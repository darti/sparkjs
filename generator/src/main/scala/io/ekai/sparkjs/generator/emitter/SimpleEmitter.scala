package io.ekai.sparkjs.generator.emitter

class SimpleEmitter {

  def emit(ts: TsEntity): String = ts match {
    case TsType(name, path, args) =>
      s"""$name${if (args.nonEmpty)s"""<${args.map(emit) mkString ", "}>""" else ""}"""

    case TsParameters(parameters) =>
      parameters.map(p => s"""${p.name} : ${emit(p.typ)}""") mkString ", "

    case TsMethod(name, returnType, parameters) =>
      s"""
         |public $name(${emit(parameters)}) : ${emit(returnType)} {}
     """.stripMargin

    case TsClass(name, methods) =>
      s"""export class $name {
         |  ${methods map emit mkString "\n"}
         |}
     """.stripMargin
  }
}
