package io.ekai.sparkjs.generator.emitter

import scala.reflect.runtime.universe._

trait TypeFilter {

  def filter(t: Type): Boolean

}
