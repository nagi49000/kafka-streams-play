import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.kafkastreamsplay"
ThisBuild / organizationName := "kafkastreamsplay"

val kafkaVersion = "3.1.0"
val slf4jVersion = "1.7.36"

val streamDependencies = Seq(
  "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion,
  "org.slf4j" % "slf4j-simple" % slf4jVersion
)

lazy val root = (project in file("."))
  .settings(
    name := "processor",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= streamDependencies
  )

// Uncomment the following for publishing to Sonatype.
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for more detail.

ThisBuild / description := "Simple example of a kafka streams processor."
ThisBuild / licenses    := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage    := Some(url("https://github.com/nagi49000/kafka-streams-play"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/nagi49000/kafka-streams-play"),
    "scm:git@github.com/nagi49000/kafka-streams-play.git"
  )
)

// dirty workaround for deduplication in Jackson
assembly / assemblyMergeStrategy := {
      case "module-info.class" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }

// ThisBuild / developers := List(
//   Developer(
//     id    = "Your identifier",
//     name  = "Your Name",
//     email = "your@email",
//     url   = url("http://your.url")
//   )
// )
// ThisBuild / pomIncludeRepository := { _ => false }
// ThisBuild / publishTo := {
//   val nexus = "https://oss.sonatype.org/"
//   if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
//   else Some("releases" at nexus + "service/local/staging/deploy/maven2")
// }
// ThisBuild / publishMavenStyle := true
