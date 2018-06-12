package io.ekai.sparkjs.generator

import java.nio.file.Paths

import io.ekai.sparkjs.generator.emitter.Parser
import org.apache.spark.sql.SparkSession

import scala.reflect.runtime.universe._

object SparkReflection extends App {

  import io.circe.generic.auto._
  import io.circe.syntax._

  val tsSparkSession = Parser(typeOf[SparkSession])
  val output = Paths.get("../lib/generated", "definitions.json")

  val json = tsSparkSession.asJson

  scala.tools.nsc.io.File(output.toString).writeAll(json.toString())

}
