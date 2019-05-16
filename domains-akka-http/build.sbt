libraryDependencies += "com.thoughtworks.dsl" %% "dsl" % "1.2.0"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

enablePlugins(Example)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.7" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.21" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.8" % Test

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-return" % "1.2.0" % Test

libraryDependencies += "com.thoughtworks.dsl" %% "keywords-await" % "1.2.0" % Test

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-bangnotation" % "1.2.0")

addCompilerPlugin("com.thoughtworks.dsl" %% "compilerplugins-reseteverywhere" % "1.2.0")

import meta._

exampleSuperTypes += ctor"_root_.akka.http.scaladsl.testkit.ScalatestRouteTest"
