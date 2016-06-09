
lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra := (
    <url>https://github.com/divergence082/HDFS-Logger</url>
      <licenses>
        <license>
          <name>BSD-style</name>
          <url>http://www.opensource.org/licenses/bsd-license.php</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:divergence082/HDFS-Logger.git</url>
        <connection>scm:git:git@github.com:divergence082/HDFS-Logger.git</connection>
      </scm>
      <developers>
        <developer>
          <id>divergence082</id>
          <name>Valeria Kononenko</name>
          <email>divergence082@gmail.com</email>
          <url>https://github.com/divergence082</url>
        </developer>
      </developers>)
)


lazy val hdfsLogger = Project(
  id = "hdfs-logger",
  base = file("."),
  settings = Defaults.coreDefaultSettings ++ Defaults.itSettings ++ publishSettings ++ Seq(
    organization := "space.divergence",
    name := "hdfs-logger",
    version := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "ch.qos.logback"    %  "logback-classic" % "1.1.7",
      "org.apache.hadoop" %  "hadoop-client"   % "2.7.2"
        exclude("org.slf4j", "slf4j-log4j12"),
      "org.scalatest"     %% "scalatest"       % "3.0.0-RC1" % "it,test")))
  .configs(IntegrationTest)
  .settings(
    testOptions in Test := Seq(Tests.Filter(s => s.endsWith("Test"))))
  .settings(
    scalastyleConfig in Compile := baseDirectory.value / "project" / "scalastyle-config.xml",
    scalastyleConfig in Test := baseDirectory.value / "project" / "scalastyle-config.xml")
  .settings(
    test in assembly := {},
    mainClass in assembly := Some("space.divergence.hdfs.logger.Logger"),
    assemblyJarName in assembly := s"${name.value}-${version.value}.jar",
    assemblyMergeStrategy in assembly := {
      case PathList("javax", "servlet", xs @ _*)        => MergeStrategy.first
      case PathList("javax", "activation", xs @ _*)     => MergeStrategy.first
      case PathList("org", "apache", xs @ _*)           => MergeStrategy.last
      case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
      case PathList("plugin.properties")                => MergeStrategy.last
      case x => (assemblyMergeStrategy in assembly).value(x)
    }
  )
