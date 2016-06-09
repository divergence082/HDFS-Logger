# HDFS-Logger

Module for log stream redirection to HDFS


Sbt:
----
```
libraryDependencies += "space.divergence" % "hdfs-logger_2.11" % "0.0.1"
```
or
```
libraryDependencies += "space.divergence" %% "hdfs-logger" % "0.0.1"
```


Usage:
------
- with sbt:
```
cat src/it/resources/test.txt | sbt "run-main space.divergence.hdfs.logger.Logger hdfs://namenode.host:9000 /space.divergence.hdfs.logger.Logger.sbt 10"
```

- with jar
```
sbt assembly
cat src/it/resources/test.txt | java -jar target/scala-2.11/hdfs-logger-0.0.1.jar hdfs://namenode.host:9000 /space.divergence.hdfs.logger.Logger.jar 10
```

- in code
```
import java.io.FileInputStream
import space.divergence.hdfs.logger.Logger

val logger = Logger("hdfs://namenode.host:9000")
var stream = new FileInputStream("src/it/resources/test.txt")

logger.log("/space.divergence.hdfs.logger.Logger.code", stream, 10)
```

Tests:
------
```
sbt "it:test-only space.divergence.hdfs.logger.LoggerTest -- -DhdfsUri=hdfs://namenode.host:9000 -DhdfsPath=/space.divergence.hdfs.logger.LoggerTest -DfsPath=src/it/resources/test.txt -DchunkLen=10 -DtestTime=500"
```