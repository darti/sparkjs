package io.ekai.sparkjs.generator

package object emitter {

  sealed trait TsEntity

  case object TsNothing extends TsEntity


  case class TsType(name: String, path: List[String], args: List[TsType]) extends TsEntity

  case class TsParameter(name: String, typ: TsType) extends TsEntity


  case class TsClass(
                      typ: TsType,
                      static: Boolean,
                      methods: Seq[TsMethod],
                      innerClasses: Seq[TsClass]
                    ) extends TsEntity

  case class TsMethod(name: String, static: Boolean, returnType: TsType, parameters: List[TsParameter] = List.empty) extends TsEntity

}
