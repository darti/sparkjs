package io.ekai.sparkjs.generator

import scala.reflect.runtime.universe._

class TypeScriptEmitter {

}

case class TsMethod(name: String, returnType: Type, parameters: List[List[Symbol]]) {

  def emitType(t: Type): Object = {
    s"""${t.typeSymbol.name}${if(t.typeArgs.nonEmpty)s"""<${t.typeArgs mkString ", "}>""" else ""}"""

  }

  def emitParameters(): List[String] = {
    parameters.flatten.map(p => s"""${p.name} : ${emitType(p.typeSignature)}""")
  }

  def emit(): String = {
    s"""
       |public $name(${emitParameters() mkString ", "}) : ${emitType(returnType)} {
       |
       |}
     """.stripMargin
  }
}

