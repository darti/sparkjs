package io.ekai.sparkjs.generator

import org.apache.spark.sql.SparkSession

object Test extends App {
  val spark = SparkSession.builder()
    .master("local")
    .appName("SparkJS")
    .getOrCreate()

  val df = spark.read.json("examples/people.json")

  df.sparkSession.implicits.encod

}
