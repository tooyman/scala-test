ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

val sparkVersion = "3.2.2"
val cobrixVersion = "2.6.2"

lazy val root = (project in file("."))
  .settings(
    name := "scala-test"

  )
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "10.1.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "za.co.absa.cobrix" %% "spark-cobol" % cobrixVersion
