name := "hdfs-logger"

organization := "livetex"

version := "0.0.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % "2.6.0"
)

initialCommands := "import hdfslogger._"