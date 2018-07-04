package io.ekai.sparkjs.generator

package object emitter {

  sealed trait TsEntity

  case object TsNothing extends TsEntity


  case class TsType(name: String, path: List[String], args: List[TsType]) extends TsEntity

  case class TsParameter(name: String, typ: TsType) extends TsEntity


  case class TsClass(
                      typ: TsType,
                      methods: Seq[TsMethod],
                      staticMethods: Seq[TsMethod],
                      innerClasses: Seq[TsClass],
                      staticInnerClasses: Seq[TsClass]
                    ) extends TsEntity

  case class TsMethod(name: String, returnType: TsType, parameters: List[TsParameter] = List.empty) extends TsEntity

}
