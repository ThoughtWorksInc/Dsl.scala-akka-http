package com.thoughtworks.dsl.keywords.akka.http
import akka.http.scaladsl.server._
import com.thoughtworks.dsl.Dsl
import scala.language.implicitConversions

case class TApply[T](directive: Directive[T]) extends AnyVal with Dsl.Keyword[TApply[T], T]

object TApply {

  implicit def implicitlyTApply[T](directive: Directive[T]): TApply[T] = TApply(directive)

  implicit def tApplyDsl[T]: Dsl[TApply[T], Route, T] = new Dsl[TApply[T], Route, T] {
    def cpsApply(keyword: TApply[T], handler: T => Route): Route = {
      keyword.directive.tapply(handler)
    }
  }

}