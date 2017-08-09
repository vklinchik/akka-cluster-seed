import sbt._

val akkaVersion = "2.5.3"

lazy val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor"                 % akkaVersion,
  "com.typesafe.akka" %% "akka-remote"                % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster"               % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools"         % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib"               % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit"               % akkaVersion   % "test",
  "com.typesafe.akka" %% "akka-cluster-metrics"       % akkaVersion
)

lazy val libraryDeps = Seq(
  "org.scalatest"     %% "scalatest"                  % "3.0.1"       % "test"
)

lazy val allDeps = akkaDeps ++ libraryDeps

lazy val commonSettings = Seq(
  name := """akka-cluster-seed""",
  version := "1.0",
  scalaVersion := "2.12.3",
  libraryDependencies := allDeps
)

lazy val commons =
  (project in file("commons"))
    .settings(commonSettings: _*)
    .settings(
      name := "commons"
    )

lazy val seed =
  (project in file("seed"))
    .settings(commonSettings: _*)
    .settings(
      name := "seed"
    )
    .aggregate(commons)
    .dependsOn(commons)

lazy val master =
  (project in file("master"))
    .settings(commonSettings: _*)
    .settings(
      name := "master"
    )
    .aggregate(commons)
    .dependsOn(commons)

lazy val worker =
  (project in file("worker"))
    .settings(commonSettings: _*)
    .settings(
      name := "worker"
    )
    .aggregate(commons)
    .dependsOn(commons)

lazy val client =
  (project in file("client"))
    .settings(commonSettings: _*)
    .settings(
      name := "master"
    )
    .aggregate(commons)
    .dependsOn(commons)

lazy val root =
  (project in file("."))
    .settings(commonSettings: _*)
    .settings(
      name := "akka-cluster-seed"
    )
    .aggregate(commons, seed, master, worker, client)
    .dependsOn(commons, seed, master, worker, client)