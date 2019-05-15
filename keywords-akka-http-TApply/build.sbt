libraryDependencies += "com.thoughtworks.dsl" %% "dsl" % "1.2.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

enablePlugins(Example)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.7" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.21" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8" % Test

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-using" % "1.2.0" % Optional

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.1.1")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.1.1")

import meta._

exampleSuperTypes += ctor"_root_.akka.http.scaladsl.testkit.ScalatestRouteTest"

exampleSuperTypes += ctor"_root_.akka.http.scaladsl.server.Directives"
