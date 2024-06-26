scalaVersion := "2.12.19"

libraryDependencies += "org.scalameta" %% "munit" % "1.0.0-M12" % Test

// Reproduce https://github.com/stryker-mutator/stryker4s/issues/726
(Compile / scalaSource) := baseDirectory.value / "src" / "main" / "scala"
(Compile / javaSource) := baseDirectory.value / "src" / "main" / "scala"
