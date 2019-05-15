package com.thoughtworks.dsl.keywords.akka.http
import akka.http.scaladsl.server._
import com.thoughtworks.dsl.Dsl
import scala.language.implicitConversions

/** This [[TApply]] keyword can be used to extract the value of [[akka.http.scaladsl.server.Directive]]s, without additional indentation levels of curly brackets.
  *
  * @note This [[TApply]] class can be found in the following library:
  *
  *       <pre>
  *       libraryDependencies += "com.thoughtworks.dsl" %% "keywords-akka-http-tapply" % "latest.release"
  *       </pre>
  *
  * @example Ordinary Akka HTTP DSL requires additional brackets to perform any directive.
  *
  *          For example, the following `extractParametersWithBrackets` extracts query parameters `p1` and `p2` with the help of the directive [[akka.http.scaladsl.server.Directives.parameters]].
  *
  *          {{{
  *          def extractParametersWithBrackets = {
  *            get {
  *              parameters("p1", "p2") { (p1, p2) =>
  *                complete(s"$p1, $p2!")
  *              }
  *            }
  *          }
  *
  *          Get("/?p1=Hello&p2=World") ~> extractParametersWithBrackets ~> check {
  *            responseAs[String] should be("Hello, World!")
  *          }
  *          }}}
  *
  *          The brackets can be avoided with the help of this [[TApply]] keyword:
  *
  *          {{{
  *          def extractParametersWithoutBrackets = {
  *            get {
  *              val (p1, p2) = !TApply(parameters("p1", "p2"))
  *              complete(s"$p1, $p2!")
  *            }
  *          }
  *
  *          Get("/?p1=Hello&p2=World") ~> extractParametersWithoutBrackets ~> check {
  *            responseAs[String] should be("Hello, World!")
  *          }
  *          }}}
  *
  * @example In addition, this [[TApply]] keyword can be used together with other keywords.
  *
  *          For example, you can use [[com.thoughtworks.dsl.keywords.Using]] to automatically manage resources.
  *
  *          {{{
  *          import com.thoughtworks.dsl.keywords.Using
  *          import com.thoughtworks.dsl.keywords.akka.http.TApply
  *          import java.nio.file.Files.newDirectoryStream
  *          import java.nio.file.Paths
  *          import scala.collection.JavaConverters._
  *
  *          def currentDirectoryRoute = pathPrefix("current-directory") {
  *            get {
  *              val Tuple1(glob) = !TApply(parameters("glob"))
  *              val currentDirectory = !Using(newDirectoryStream(Paths.get(""), glob))
  *              path("file-names") {
  *                complete(currentDirectory.iterator.asScala.map(_.toString).mkString(","))
  *              } ~ path("number-of-files") {
  *                complete(currentDirectory.iterator.asScala.size.toString)
  *              }
  *            }
  *          }
  *          }}}
  *
  *          With the help of the `Using` keyword, the `currentDirectoryRoute` will open the current directory according to the glob pattern extract from the query parameter, and automatically close the directory after the HTTP request is processed completely.
  *
  *          {{{
  *          Get("/current-directory/file-names?glob=*.md") ~> currentDirectoryRoute ~> check {
  *            responseAs[String] should be("README.md")
  *          }
  *          Get("/current-directory/file-names?glob=*.*") ~> currentDirectoryRoute ~> check {
  *            responseAs[String].split(',') should (contain("secret.sbt") and contain("build.sbt") and contain("README.md"))
  *          }
  *          Get("/current-directory/number-of-files?glob=*.sbt") ~> currentDirectoryRoute ~> check {
  *            responseAs[String] should be("2")
  *          }
  *          }}}
  */
case class TApply[L](directive: Directive[L]) extends AnyVal with Dsl.Keyword[TApply[L], L]

object TApply {

  implicit def implicitTApply[L](directive: Directive[L]): TApply[L] = TApply(directive)

  implicit def tApplyDsl[L]: Dsl[TApply[L], Route, L] = new Dsl[TApply[L], Route, L] {
    def cpsApply(keyword: TApply[L], handler: L => Route): Route = {
      keyword.directive.tapply(handler)
    }
  }

}
