ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.8"

val sparkVersion = "3.3.0"


lazy val root = (project in file("."))
  .settings(
    name := "CommonCrawl",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0",
      "org.apache.logging.log4j" % "log4j-api" % "2.19.0" % Runtime,
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.19.0" % Runtime,
      "org.apache.logging.log4j" % "log4j-core" % "2.19.0" % Runtime,
      "org.netpreserve" % "jwarc" % "0.20.0",
      "org.apache.hadoop" % "hadoop-common" % "3.3.2",
      "org.apache.hadoop" % "hadoop-client" % "3.3.2",
      "org.apache.hadoop" % "hadoop-aws" % "3.3.2",
      "org.elasticsearch" % "elasticsearch-spark-30_2.12" % "8.5.1",
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
