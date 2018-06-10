package example

import org.apache.spark.sql.SparkSession

import scala.reflect.runtime.universe._

object Hello extends App {

  implicit class TsType(t: Type) {
    def toTs: String = t.getClass.getSimpleName
  }

  def process(sym: MethodSymbol) = {
    s"""
       | public ${sym.name}() : ${sym.returnType.toTs} {
       | }
       """.stripMargin
  }

  def process(sym: TermSymbol) = {
    s"""
       | public ${sym.name}() : ${sym.typeSignature.toTs} {
       | }
       """.stripMargin
  }

  val members = typeOf[SparkSession].decls
    .filter(m => m.isPublic && m.isTerm)
    .map(m => (m.isMethod) match {
      case true => process(m.asMethod)
      case _ => process(m.asTerm)
    })

  val source = members mkString "\n\n"

  println(source)
}
