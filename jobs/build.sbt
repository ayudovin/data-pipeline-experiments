scalaVersion := "2.12.10"

name := "jobs"
organization := "ch.epfl.scala"
version := "1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.1.2" % "provided",
  "org.apache.spark" %% "spark-mllib" % "3.1.2" % "provided",
)