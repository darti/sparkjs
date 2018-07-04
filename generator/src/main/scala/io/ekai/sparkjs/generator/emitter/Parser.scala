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
        val newDone = done + h


        if (shouldProcess(h)) {
          println(h)
          val cls = walk(h, false)
          rec(todo.tail ++ (cls.dependencies -- newDone), newDone, cls.entity :: result)
        }
        else {
          rec(todo.tail, newDone, result)
        }
      }

    }

    rec(rootTypes.toSet, Set.empty, List.empty)._3
  }

  def walk(t: Type, static: Boolean): ParserNode[TsClass] = {


    val (met, inner) = members(t, false)
    val (staticMet, staticInner) = members(t.companion, true)

    val cls = ParserNode.merge(process(t), met, inner, staticMet, staticInner)

    val tsCls = TsClass(
      cls.entity,
      static,
      (met ++ staticMet).map(_.entity),
      (inner ++ staticInner).map(_.entity))

    ParserNode(tsCls, cls.dependencies)
  }

  private def members(t: Type, static: Boolean): (List[ParserNode[TsMethod]], List[ParserNode[TsClass]]) =
    scala.util.Try(
      t.decls
        .filter(_.isPublic)
        .map(m => {
          if (m.isMethod)
            (Some(process(m.asMethod, static)), None)
          else if (m.isTerm)
            (Some(process(m.asTerm, static)), None)
          else if (m.isClass)
            (None, Some(process(m.asClass, static)))
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
        }).getOrElse((List.empty, List.empty))


  private def process(sym: MethodSymbol, static: Boolean): ParserNode[TsMethod] = {
    val ret = process(sym.returnType)
    val boundTypes = sym.typeParams.map(_.asType)

    val params = sym.paramLists.flatten.map(s => {
      val p = process(s.typeSignature, boundTypes.map(_.toType))

      ParserNode(TsParameter(
        s.name.decodedName.toString,
        p.entity),
        p.dependencies)
    })

    val method = TsMethod(
      sym.name.toString,
      static,
      ret.entity,
      params.map(_.entity))


    ParserNode.merge(
      method,
      params,
      sym.returnType
    )
  }

  private def process(cls: ClassSymbol, static: Boolean): ParserNode[TsClass] = {
    walk(cls.selfType, static)
  }


  private def process(sym: TermSymbol, static: Boolean): ParserNode[TsMethod] = {
    val ret = process(sym.info)

    ParserNode(TsMethod(
      sym.name.toString,
      static,
      ret.entity),
      ret.dependencies)
  }

  private def process(typ: Type, boundTypes: List[Type] = List.empty): ParserNode[TsType] = {
    val args = scala.util.Try(
      typ.typeArgs
        .filter(t => !boundTypes.exists(t =:= _))
        .map(t => process(t, boundTypes)))
      .getOrElse(List.empty)


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


  def shouldProcess(typ: Type): Boolean = {
    val pkg = getPackage(typ.typeSymbol)

    pkg.size >= 3 && pkg(2) == "spark"
  }

}
