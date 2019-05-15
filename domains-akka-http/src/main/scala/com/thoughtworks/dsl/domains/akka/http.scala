package com.thoughtworks.dsl
package domains.akka
import Dsl.!!
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.util.Tuple

/** This mimicked package object [[http]] that contains [[https://github.com/akka/akka-http Akka HTTP]] support for [[https://github.com/ThoughtWorksInc/Dsl.scala Dsl.scala]].
  *
  * @note This [[http]] object can be found in the following library:
  *
  *       <pre>
  *       libraryDependencies += "com.thoughtworks.dsl" %% "domains-akka-http" % "latest.release"
  *       </pre>
  *
  */
object http {

  implicit def directiveDsl[Keyword, Value, T: Tuple](
      implicit continuationDsl: Dsl[Keyword, Route !! T, Value]
  ): Dsl[Keyword, Directive[T], Value] = new Dsl[Keyword, Directive[T], Value] {
    def cpsApply(keyword: Keyword, handler: Value => Directive[T]): Directive[T] = {
      Directive(continuationDsl.cpsApply(keyword, { value: Value =>
        handler(value).tapply
      }))
    }
  }

  implicit def standardRouteDsl[Keyword, Value](
      implicit routeDsl: Dsl[Keyword, Route, Value]
  ): Dsl[Keyword, StandardRoute, Value] = new Dsl[Keyword, StandardRoute, Value] {
    def cpsApply(keyword: Keyword, handler: Value => StandardRoute): StandardRoute = {
      StandardRoute(routeDsl.cpsApply(keyword, handler))
    }
  }

}
