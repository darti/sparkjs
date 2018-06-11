package io.ekai.sparkjs.generator.emitter

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror


class Parser {

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

  def process(sym: MethodSymbol): TsMethod =
    TsMethod(
      sym.name.toString,
      process(sym.returnType),
      TsParameters(
        sym.paramLists.flatten.map(s => TsParameter(
          s.name.decodedName.toString,
          process(s.typeSignature)))
      ))


  def process(sym: TermSymbol): TsMethod =
    TsMethod(
      sym.name.toString,
      process(sym.info))

  def process(typ: Type): TsType =
    TsType(
      typ.typeSymbol.name.toString,
      getPackage(typ.typeSymbol),
      typ.typeArgs.map(process)
    )

  def getPackage(sym: Symbol): List[String] = {
    def getPackageRec(s: Symbol): List[String] =
      if (s == NoSymbol) List.empty
      else if (s.isPackage) s.fullName.split("\\.").toList
      else getPackageRec(s.owner)

    getPackageRec(sym)
  }

}
