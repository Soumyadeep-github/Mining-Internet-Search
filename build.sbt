ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.8"

val sparkVersion = "3.3.0"


lazy val root = (project in file("."))
  .settings(
    name := "CommonCrawl",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0",
      "org.apache.logging.log4j" % "log4j-api" % "2.19.0" % Runtime,
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.19.0" % Runtime,
      "org.apache.logging.log4j" % "log4j-core" % "2.19.0" % Runtime,
      "org.netpreserve" % "jwarc" % "0.18.1",
      "org.apache.hadoop" % "hadoop-common" % "3.3.2",
      "org.apache.hadoop" % "hadoop-client" % "3.3.2",
      "org.apache.hadoop" % "hadoop-aws" % "3.3.2",
      "commons-httpclient" % "commons-httpclient" % "20020423",
    ),
//    Compile  / mainClass    := Some("com.scalaWARC.tabNineWarcReaderScala")
//    assembly / assemblyMergeStrategy := {
//      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//      case x => MergeStrategy.first
//    },
//    assembly / assemblyJarName := "scala-warc-emr-fatjar-1.0.jar",
//    assembly / assemblyOutputPath := file(s"./runnable-jar/${(assembly / assemblyJarName).value}")

  )
