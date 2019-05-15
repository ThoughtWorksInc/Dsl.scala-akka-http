organization in ThisBuild := "com.thoughtworks.dsl"

lazy val `domains-akka-http` = project.dependsOn(`keywords-akka-http-TApply` % Test)

lazy val `keywords-akka-http-TApply` = project

enablePlugins(ScalaUnidocPlugin)

publish / skip := false
