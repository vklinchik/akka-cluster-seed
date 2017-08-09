import sbt.{Tests, _}

val akkaVersion = "2.5.3"
val akkaHttpVersion = "10.0.9"

/*
scalacOptions ++= Seq(
  "-language:_",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code"
) */

lazy val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor"                 % akkaVersion,
  "com.typesafe.akka" %% "akka-remote"                % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster"               % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools"         % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib"               % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit"               % akkaVersion   % "test",
  "com.typesafe.akka" %% "akka-cluster-metrics"       % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"                 % akkaVersion
)

lazy val akkaHttpDeps = Seq(
  "com.typesafe.akka"     %% "akka-http-core"         % akkaHttpVersion,
  "com.typesafe.akka"     %% "akka-http"              % akkaHttpVersion,
  "com.typesafe.akka"     %% "akka-http-testkit"      % akkaHttpVersion,
  "com.typesafe.akka"     %% "akka-http-spray-json"   % akkaHttpVersion
)

lazy val libraryDeps = Seq(
  "org.scalatest"     %% "scalatest"                  % "3.0.1"       % "test"
)
/*
lazy val postgresDeps = {
  Seq(
    "com.typesafe.slick"  %%  "slick"               % "3.1.1",
    "com.typesafe.slick"  %%  "slick-hikaricp"      % "3.1.1",
    "com.zaxxer"          %%  "HikariCP"            % "2.4.5"
  )
} */
/*
lazy val testSettings = Seq(
  testOptions in Test += Tests.Argument("-oF"),
  parallelExecution in Test := false
)
*/
lazy val allDeps = akkaDeps ++ akkaHttpDeps ++ libraryDeps //++ postgresDeps


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

lazy val api =
  (project in file("api"))
    .settings(commonSettings: _*)
    .settings(
      name := "api"
    )
    .aggregate(commons)


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
    .aggregate(commons, seed, api, master, worker, client)
    .dependsOn(commons, seed, api, master, worker, client)