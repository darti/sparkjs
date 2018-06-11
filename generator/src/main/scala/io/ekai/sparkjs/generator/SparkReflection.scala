package io.ekai.sparkjs.generator

import java.nio.file.Paths

import io.ekai.sparkjs.generator.emitter.{Parser, SimpleEmitter}
import org.apache.spark.sql.SparkSession

import scala.reflect.runtime.universe._

object SparkReflection extends App {

  val tsSparkSession = new Parser().walk(typeOf[SparkSession])
  val output = Paths.get("../lib/generated", s"${tsSparkSession.name}.ts")

  val emitter = new SimpleEmitter()
  scala.tools.nsc.io.File(output.toString).writeAll(emitter.emit(tsSparkSession))

}
