package io.ekai.sparkjs.generator

import org.apache.spark.sql.SparkSession

import scala.reflect.runtime.universe._

object SparkReflection extends App {

  def process(sym: MethodSymbol) = {
    TsMethod(sym.name.toString, sym.returnType, sym.paramLists)
  }

  def process(sym: TermSymbol) = {
    TsMethod(sym.name.toString, sym.info, List.empty)
  }

  val sparkSessionType = typeOf[SparkSession].decls
    .filter(_.isPublic)
    .map(m => m.isMethod match {
      case true => process(m.asMethod)
      case _ => process(m.asTerm)
    })


  println(sparkSessionType.map(_.emit()) mkString "\n\n")

  //  sparkSessionType.decls
  //    .filter(_.isTerm)
  //    .map(_.asTerm)
  //    .filter(m => (m.isVal || m.isMethod) && ! m.isConstructor)
  //    .map(m => m.asTerm)
}
