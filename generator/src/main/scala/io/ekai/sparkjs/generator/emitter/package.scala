package io.ekai.sparkjs.generator

package object emitter {

  sealed trait TsEntity


  case class TsType(name: String, path: List[String], args: List[TsType]) extends TsEntity

  case class TsParameter(name: String, typ: TsType) extends TsEntity


  case class TsClass(typ: TsType, methods: Seq[TsMethod]) extends TsEntity

  case class TsMethod(name: String, returnType: TsType, parameters: List[TsParameter] = List.empty) extends TsEntity

}
