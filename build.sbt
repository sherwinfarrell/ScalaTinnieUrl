name := "UrlShortener"

version := "0.1"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

herokuAppName in Compile := "radiant-ravine-94917"



libraryDependencies ++= Seq("com.typesafe.slick" %% "slick" % "3.2.3")

libraryDependencies += guice

val circeVersion = "0.12.3"

scalacOptions += "-Ypartial-unification"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.5.0"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.3.2",
  "com.h2database"  %  "h2"                % "1.4.197",
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.postgresql" % "postgresql" % "42.2.11",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "com.dripower" %% "play-circe" % "2712.0",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"
)