import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.3-RC5"

  // uncomment the following if you want to use the snapshot repo
  val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil

  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" %% "specs" % "1.6.5" % "test->default",
    "com.h2database" % "h2" % "1.2.138",
    "mysql" % "mysql-connector-java" % "5.1.13" % "compile->default",
    "log4j" % "log4j" % "1.2.16" % "compile->default",
    "org.slf4j" % "slf4j-log4j12" % "1.6.1" % "compile->default"
  ) ++ super.libraryDependencies
}
