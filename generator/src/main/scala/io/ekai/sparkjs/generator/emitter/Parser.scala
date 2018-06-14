package io.ekai.sparkjs.generator.emitter


import scala.reflect.runtime.universe._


object Parser {

  case class ParserNode[E <: TsEntity](entity: E, dependencies: Set[Type] = Set.empty)

  object ParserNode {
    def merge[E <: TsEntity](entity: E, subNodes: Seq[ParserNode[_ <: TsEntity]], dependencies: Type*): ParserNode[E] =
      ParserNode(
        entity,
        subNodes.map(_.dependencies).fold(dependencies.toSet)(_ ++ _)
      )

    def merge[E <: TsEntity](node: ParserNode[E], subNodes: Seq[ParserNode[_ <: TsEntity]]): ParserNode[E] =
      ParserNode.merge(
        node.entity,
        subNodes,
        (node.dependencies.toSeq: _*)
      )
  }


  def apply(rootTypes: Type*): Seq[TsClass] = {

    def rec(todo: Set[Type], done: Set[Type], result: List[TsClass]): (Set[Type], Set[Type], List[TsClass]) = {
      if (todo.isEmpty) (todo, done, result)
      else {
        val h = todo.head
        val cls = walk(h)
        val newDone = done + h

        println(h)

        rec(todo.tail ++ (cls.dependencies -- newDone), newDone, cls.entity :: result)
      }

    }

    rec(rootTypes.toSet, Set.empty, List.empty)._3
  }

  def walk(t: Type): ParserNode[TsClass] = {


    val methods = t.decls
      .filter(_.isPublic)
      .map(m => m.isMethod match {
        case true => process(m.asMethod)
        case _ => process(m.asTerm)
      }).toSeq

    val cls = ParserNode.merge(process(t), methods)

    val tsCls = TsClass(
      cls.entity,
      methods.map(_.entity))

    ParserNode(tsCls, cls.dependencies)
  }

  private def process(sym: MethodSymbol): ParserNode[TsMethod] = {
    val ret = process(sym.returnType)

    val params = sym.paramLists.flatten.map(s => {
      val p = process(s.typeSignature)

      ParserNode(TsParameter(
        s.name.decodedName.toString,
        p.entity),
        p.dependencies)
    })

    val method = TsMethod(
      sym.name.toString,
      ret.entity,
      params.map(_.entity))


    ParserNode.merge(
      method,
      params,
      sym.returnType
    )
  }


  private def process(sym: TermSymbol): ParserNode[TsMethod] = {
    val ret = process(sym.info)

    ParserNode(TsMethod(
      sym.name.toString,
      ret.entity),
      ret.dependencies)
  }

  private def process(typ: Type): ParserNode[TsType] = {
    val args = typ.typeArgs.map(process)

    val tsType = TsType(
      typ.typeSymbol.name.toString,
      getPackage(typ.typeSymbol),
      args.map(_.entity))

    ParserNode.merge(tsType, args, typ)
  }

  private def getPackage(sym: Symbol): List[String] = {
    def getPackageRec(s: Symbol): List[String] =
      if (s == NoSymbol) List.empty
      else if (s.isPackage) s.fullName.split("\\.").toList
      else getPackageRec(s.owner)

    getPackageRec(sym)
  }

}
