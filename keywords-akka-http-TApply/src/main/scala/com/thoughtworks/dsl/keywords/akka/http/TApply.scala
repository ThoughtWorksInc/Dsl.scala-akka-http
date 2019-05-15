package com.thoughtworks.dsl.keywords.akka.http
import akka.http.scaladsl.server._
import com.thoughtworks.dsl.Dsl
import scala.language.implicitConversions

/** The [[Dsl.Keyword]] to perform a [[akka.http.scaladsl.server.Directive]]
  *
  * @note This [[TApply]] class can be found in the following library:
  *
  *       <pre>
  *       libraryDependencies += "com.thoughtworks.dsl" %% "keywords-akka-http-tapply" % "latest.release"
  *       </pre>
  *
  * @see [[com.thoughtworks.dsl.domains.akka.http]] for usage.
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
