import sbt._
import Keys._

object Play2AngularInitBuild extends Build {
  val _name         = "Play2-AngularInit"
  val _organization = "com.github.rabitarochan"
  val _version      = "0.1"
  val _scalaVersion = "2.11.5"

  def _publishTo(v: String) = {
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) {
      Some("snapshots" at nexus + "content/repositories/snapshots")
    } else {
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }
  }

  lazy val baseSettings = Seq(
    organization := _organization,
    version := _version
  )

  lazy val publishSettings = Seq(
    publishMavenStyle := true,
    publishTo <<= version(_publishTo),
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/rabitarochan/Play2-AngularInit</url>
      <licenses>
        <license>
          <name>Apache License, Version 2.0</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:rabitarochan/Play2-AngularInit.git</url>
        <connection>scm:git:git@github.com:rabitarochan/Play2-AngularInit.git</connection>
      </scm>
      <developers>
        <developer>
          <id>rabitarochan</id>
          <name>Kengo Asamizu</name>
          <url>https://github.com/rabitarochan</url>
        </developer>
      </developers>
    )
  )

  lazy val root = Project("root", file("."))
    .settings(baseSettings : _*)
    .aggregate(core/*, example*/)

  lazy val core = Project("core", file("core"))
    .settings(baseSettings : _*)
    .settings(publishSettings : _*)
    .settings(
      name := _name,
      scalaVersion := _scalaVersion,
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play" % "2.3.7" % "provided",
        "org.scalatest" %% "scalatest" % "2.2.1" % "test"
      )
    )

  // lazy val example = Project("example", file("example"))
  //   .enablePlugins(PlayScala)
  //   .settings(
  //     scalaVersion := _scalaVersion,
  //     libraryDependencies ++= Seq(
  //       "com.typesafe.play" %% "play" % "2.3.4" % "provided"
  //     )
  //   ).dependsOn(core)

}