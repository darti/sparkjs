package io.ekai.sparkjs.generator.emitter


import scala.reflect.runtime.universe._


object Parser {


  def apply(rootTypes: Type*): Seq[TsClass] = {

    def rec(todo: Set[Type], done: Set[Type], result: List[TsClass]): (Set[Type], Set[Type] , List[TsClass]) = {
      if (todo.isEmpty) (todo, done, result)
      else {
        val h = todo.head
        val (cls, deps) = walk(h)
        val newDone = done + h

        println(h)

        rec(todo.tail ++ (deps -- newDone), newDone, cls :: result)
      }

    }

    rec(rootTypes.toSet, Set.empty, List.empty)._3
  }

  def walk(t: Type): (TsClass, Set[Type]) = {
    val (self, deps) = process(t)
    val methods = t.decls
      .filter(_.isPublic)
      .map(m => m.isMethod match {
        case true => process(m.asMethod)
        case _ => process(m.asTerm)
      })

    (TsClass(
      self,
      methods.map(_._1).toSeq),
      methods.map(_._2).fold(Set.empty)(_ ++ _) & deps)
  }

  private def process(sym: MethodSymbol): (TsMethod, Set[Type]) = {
    val (ret, deps) = process(sym.returnType)
    val params = sym.paramLists.flatten.map(s => {
      val (p, d) = process(s.typeSignature)

      (TsParameter(
        s.name.decodedName.toString,
        p),
        d)
    })


    (TsMethod(
      sym.name.toString,
      ret,
      TsParameters(
        params.map(_._1)
      )),
      params.map(_._2).fold(Set.empty)(_ ++ _) & deps)
  }


  private def process(sym: TermSymbol): (TsMethod, Set[Type]) = {
    val (ret, deps) = process(sym.info)

    (TsMethod(
      sym.name.toString,
      ret),
      deps)
  }

  private def process(typ: Type): (TsType, Set[Type]) = {
    val args = typ.typeArgs.map(process)

    (TsType(
      typ.typeSymbol.name.toString,
      getPackage(typ.typeSymbol),
      args.map(_._1)),
      args.map(_._2).fold(Set.empty)(_ ++ _) + typ
    )
  }

  private def getPackage(sym: Symbol): List[String] = {
    def getPackageRec(s: Symbol): List[String] =
      if (s == NoSymbol) List.empty
      else if (s.isPackage) s.fullName.split("\\.").toList
      else getPackageRec(s.owner)

    getPackageRec(sym)
  }

}
