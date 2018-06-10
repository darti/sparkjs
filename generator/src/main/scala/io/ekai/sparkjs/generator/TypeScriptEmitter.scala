package io.ekai.sparkjs.generator

import scala.reflect.runtime.universe._

object TypeScriptEmitter {

  def walk(t: Type): TsClass = {
    TsClass(
      t.typeSymbol.name.toString,

    t.decls
      .filter(_.isPublic)
      .map(m => m.isMethod match {
        case true => process(m.asMethod)
        case _ => process(m.asTerm)
      }).toSeq
    )
  }

  def process(sym: MethodSymbol): TsMethod = {
    TsMethod(sym.name.toString, sym.returnType, sym.paramLists)
  }

  def process(sym: TermSymbol): TsMethod = {
    TsMethod(sym.name.toString, sym.info, List.empty)
  }


}

case class TsClass(name: String, methods: Seq[TsMethod]) {
  def emit(): String = {
    s"""export class $name {
       |  ${methods map (_.emit()) mkString "\n"}
       |}
     """.stripMargin
  }
}

case class TsMethod(name: String, returnType: Type, parameters: List[List[Symbol]]) {

  def emitType(t: Type): Object = {
    s"""${t.typeSymbol.name}${if (t.typeArgs.nonEmpty)s"""<${t.typeArgs.map(emitType) mkString ", "}>""" else ""}"""

  }

  def emitParameters(): List[String] = {
    parameters.flatten.map(p => s"""${p.name} : ${emitType(p.typeSignature)}""")
  }

  def emit(): String = {
    s"""
       |public $name(${emitParameters() mkString ", "}) : ${emitType(returnType)} {}
     """.stripMargin
  }
}

