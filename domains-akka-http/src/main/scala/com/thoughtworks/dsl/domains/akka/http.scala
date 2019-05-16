package com.thoughtworks.dsl
package domains.akka
import Dsl.!!
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.util.Tuple

/** This [[http]] object provides [[Dsl]] type class instances to allow [[Dsl.Keyword]]s to be used in functions that return [[akka.http.scaladsl.server.StandardRoute]] or [[akka.http.scaladsl.server.Directive]], as long as those keywords can be used in functions that return [[scala.concurrent.Future]].
  *
  * @note This [[http]] object can be found in the following library:
  *
  *       <pre>
  *       libraryDependencies += "com.thoughtworks.dsl" %% "domains-akka-http" % "latest.release"
  *       </pre>
  * @note You don't need this [[http]] object if you only want to use a [[Dsl.Keyword]]s in a function that returns [[akka.http.scaladsl.server.Route]], because it is a type alias of [[scala.Function1]], which is supported via [[Dsl#derivedFunction1Dsl]].
  * @example You may want to import type classes in this [[http]] object when creating custom Akka HTTP [[akka.http.scaladsl.server.Directive directive]].
  *
  *          {{{
  *          import com.thoughtworks.dsl.domains.akka.http._
  *          }}}
  *
  *          Suppose you are creating a directive to asynchronously save the request body to a temporary file and return the file name.
  *
  *          {{{
  *          import java.nio.file.Path
  *          import java.nio.file.Files
  *          import com.thoughtworks.dsl.keywords.Await
  *          import com.thoughtworks.dsl.keywords.Return
  *          import com.thoughtworks.dsl.keywords.akka.http.TApply
  *          import akka.stream.scaladsl.FileIO
  *          import akka.http.scaladsl.server.Directive1
  *          import akka.http.scaladsl.server.Directives._
  *
  *          def saveRequestBody: Directive1[Path] = {
  *            val path = Files.createTempFile(null, null)
  *            val entity = !TApply(extractRequestEntity)
  *            val ioResult = !Await(entity.dataBytes.runWith(FileIO.toPath(path)))
  *            val _ = ioResult.status.get
  *            !Return(path)
  *          }
  *          }}}
  *
  *          Then the custom directive can be used in a route.
  *
  *          {{{
  *          def uploadRoute = post {
  *            val tmpPath = !TApply(saveRequestBody)
  *            complete(s"${Files.size(tmpPath)} bytes uploaded.")
  *          }
  *
  *          Post("/", ('A' to 'Z').toArray) ~> uploadRoute ~> check {
  *            responseAs[String] should be("26 bytes uploaded.")
  *          }
  *          }}}
  */
object http {

  implicit def directiveNDsl[Keyword, Value, L: Tuple](
      implicit continuationDsl: Dsl[Keyword, Route !! L, Value]
  ): Dsl[Keyword, Directive[L], Value] = new Dsl[Keyword, Directive[L], Value] {
    def cpsApply(keyword: Keyword, handler: Value => Directive[L]): Directive[L] = {
      Directive(continuationDsl.cpsApply(keyword, { value: Value =>
        handler(value).tapply
      }))
    }
  }

  implicit def directive1Dsl[Keyword, Value, T](
      implicit continuationDsl: Dsl[Keyword, Route !! T, Value]
  ): Dsl[Keyword, Directive1[T], Value] = new Dsl[Keyword, Directive1[T], Value] {
    def cpsApply(keyword: Keyword, handler: Value => Directive1[T]): Directive1[T] = {
      continuationToDirective1(continuationDsl.cpsApply(keyword, { a =>
        handler(a).apply
      }))
    }

    @inline
    private def continuationToDirective1(continuation: Route !! T): Directive[Tuple1[T]] = {
      Directive[Tuple1[T]] { (tupledHandler: Tuple1[T] => Route) =>
        continuation { a: T =>
          tupledHandler(Tuple1(a))
        }
      }
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
