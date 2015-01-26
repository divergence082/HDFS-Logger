name := "hdfs-logger"

organization := "livetex"

version := "0.0.1"

scalaVersion := "2.10.4"

val nexusHost = "sonatype-nexus.livetex.ru"
val nexusUrl = "http://" + nexusHost

resolvers += "Sonatype Nexus Repository Manager" at nexusUrl + "/nexus/content/groups/public"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % "2.6.0",
  "livetex" % "message-codec_2.10" % "1.0" withSources(),
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
)

initialCommands := "import hdfslogger._"