organization in ThisBuild := "com.thoughtworks.dsl"

name := "dsl-domains-akka-http"

libraryDependencies += "com.thoughtworks.dsl" %% "dsl" % "1.1.1"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.8"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.21" % Provided
