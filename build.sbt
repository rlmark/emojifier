name := "emojifier"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-Ypartial-unification")

libraryDependencies ++= Seq(
  "org.http4s"             %% "http4s-dsl"              % "0.20.0",
  "org.http4s"             %% "http4s-blaze-server"     % "0.20.0",
  "org.http4s"             %% "http4s-blaze-client"     % "0.20.0",
  "org.http4s"             %% "http4s-circe"            % "0.20.0",
  "org.mockito"            % "mockito-all"              % "1.10.19" % Test,
  "org.scalatest"          %% "scalatest"               % "3.0.4" % Test
)