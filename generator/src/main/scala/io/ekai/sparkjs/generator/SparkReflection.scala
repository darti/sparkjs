package io.ekai.sparkjs.generator

import java.nio.file.Paths

import org.apache.spark.sql.SparkSession

import scala.reflect.runtime.universe._

object SparkReflection extends App {

  import TypeScriptEmitter._

  val tsSparkSession = walk(typeOf[SparkSession])

  scala.tools.nsc.io.File(s"${Paths.get(tsSparkSessi on.name}.ts").writeAll(tsSparkSession.emit())

}
