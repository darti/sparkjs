package io.ekai.sparkjs.generator

package object emitter {

  trait TsEntity


  case class TsType(name: String, path: List[String], args: List[TsType]) extends TsEntity

  case class TsParameter(name: String, typ: TsType) extends TsEntity

  case class TsParameters(parameters: List[TsParameter]) extends TsEntity

  case class TsClass(name: String, methods: Seq[TsMethod]) extends TsEntity

  case class TsMethod(name: String, returnType: TsType, parameters: TsParameters = TsParameters(List.empty)) extends TsEntity

}
