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

    def merge[E <: TsEntity](node: ParserNode[E], subNodes: Seq[ParserNode[_ <: TsEntity]]*): ParserNode[E] =
      ParserNode.merge(
        node.entity,
        subNodes.flatten,
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

        /*rec*/(todo.tail ++ (cls.dependencies -- newDone), newDone, cls.entity :: result)
      }

    }

    rec(rootTypes.toSet, Set.empty, List.empty)._3
  }

  def walk(t: Type): ParserNode[TsClass] = {


    val (met, inner) = members(t)
    val (sMet, sInner) = members(t.companion)

    val cls = ParserNode.merge(process(t), met, inner, sMet, sInner)

    val tsCls = TsClass(
      cls.entity,
      met.map(_.entity),
      sMet.map(_.entity),
      inner.map(_.entity),
      sInner.map(_.entity))

    ParserNode(tsCls, cls.dependencies)
  }

  private def members(t: Type): (List[ParserNode[TsMethod]], List[ParserNode[TsClass]]) =
    t.decls
      .filter(_.isPublic)
      .map(m => {
        if (m.isMethod)
          (Some(process(m.asMethod)), None)
        else if (m.isTerm)
          (Some(process(m.asTerm)), None)
        else if (m.isClass)
          (None, Some(process(m.asClass)))
        else {
          (None, None)
        }
      })
      .foldLeft((List.empty[ParserNode[TsMethod]], List.empty[ParserNode[TsClass]])) { (ac, e) =>
      e match {
        case (Some(m), None) => (m :: ac._1, ac._2)
        case (None, Some(c)) => (ac._1, c :: ac._2)
        case (Some(m), Some(c)) => (m :: ac._1, c :: ac._2)
        case (None, None) => ac

      }
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

  private def process(cls: ClassSymbol) : ParserNode[TsClass] = {
    walk(cls.selfType)
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
