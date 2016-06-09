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
- from sbt:
```
tail -f /var/log/syslog | sbt "run-main space.divergence.hdfs.logger.Logger hdfs://namenode.host:9000 /stream.txt 1024"
```

Tests:
------
```
sbt "it:test-only space.divergence.hdfs.logger.LoggerTest -- -DhdfsUri=hdfs://namenode.host:9000 -DhdfsPath=/space.divergence.hdfs.logger.LoggerTest -DfsPath=src/it/resources/test.txt -DchunkLen=10 -DtestTime=500"
```