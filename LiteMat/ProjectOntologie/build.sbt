name := "ProjectOntologie"

version := "1.0"

scalaVersion := "2.11.12"


libraryDependencies += "org.phenoscape" %% "scowl" % "1.3"

libraryDependencies += "org.apache.jena" % "apache-jena" % "3.0.1" pomOnly()

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0"

libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"

dependencyOverrides += "com.google.guava" % "guava" % "15.0"