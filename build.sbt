name := "hdfs-logger"

organization := "livetex"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.4"

sbtVersion := "0.13.7"

val nexusHost = "sonatype-nexus.livetex.ru"
val nexusUrl = "http://" + nexusHost

resolvers += "Sonatype Nexus Repository Manager" at nexusUrl + "/nexus/content/groups/public"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-client" % "2.6.0",
  "livetex" % "message-codec_2.10" % "1.0" withSources()
)

pomIncludeRepository := { _ => false }

pomExtra := <url>https://github.com/divergence082/HDFS-Logger</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:divergence082/HDFS-Logger.git</url>
      <connection>scm:git@github.com:divergence082/HDFS-Logger.git</connection>
    </scm>
    <developers>
      <developer>
        <id>divergence082</id>
        <name>Valeria Lepina</name>
      </developer>
    </developers>

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  if (isSnapshot.value) {
    Some("snapshots" at nexusUrl + "/nexus/content/repositories/snapshots/")
  } else {
    Some("releases"  at nexusUrl + "/nexus/content/repositories/releases/")
  }
}

initialCommands := "import hdfslogger._"