package space.divergence.hdfs.logger

import java.io.FileInputStream
import scala.io.Source
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.slf4j.LoggerFactory
import org.scalatest.time.SpanSugar._
import org.scalatest.{Outcome, fixture}
import org.scalatest.concurrent.Waiters


class LoggerTest extends fixture.FunSuite with Waiters {

  val logger = LoggerFactory.getLogger(this.getClass)

  case class FixtureParam(adapter: HdfsAdapter,
                          hdfsPath: String,
                          fsPath: String,
                          chunkLength: Int,
                          testTime: Long)

  override def withFixture(test: OneArgTest): Outcome = {
    val adapter = new HdfsAdapter(test.configMap.getRequired[String]("hdfsUri"))
    val hdfsPath = test.configMap.getRequired[String]("hdfsPath")

    adapter.remove(hdfsPath)

    try {
      test(
        FixtureParam(
          adapter, hdfsPath,
          test.configMap.getRequired[String]("fsPath"),
          test.configMap.getRequired[String]("chunkLen").toInt,
          test.configMap.getRequired[String]("testTime").toLong
        ))
    } finally {
      adapter.remove(hdfsPath)
    }
  }

  test("Write bytes from file to hdfs") { f =>
    val w = new Waiter
    val stream = new FileInputStream(f.fsPath)
    val hdfsLogger = new Logger(f.adapter)
    val logF = Future(hdfsLogger.log(f.hdfsPath, stream, f.chunkLength))

    logF.onSuccess {
      case _ =>
        val source = Source.fromFile(f.fsPath).map(_.toByte).toArray
        val input = f.adapter.input(f.hdfsPath)
        val writtenSize = f.adapter.size(f.hdfsPath).toInt

        w {
          assert(
            writtenSize == source.length,
            "written file should be the same size as source file")
        }
        w.dismiss()

        val written = new Array[Byte](writtenSize)
        f.adapter.read(input, written, 0)

        w(assert(written.sameElements(source), "written file should be the same as source file"))
        w.dismiss()
    }

    logF.onFailure {
      case e => logger.error(e.getMessage)
    }

    w.await(timeout(f.testTime.millis), dismissals(2))
  }
}
