lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.padmachine",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Padmachine",
    libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"
  )
