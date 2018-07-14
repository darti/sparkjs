import { SparkSession } from './spark/sql/spark-session';

const spark = SparkSession.builder()
  .master('local')
  .appName('SparkJS')
  .getOrCreate();

const df = spark.read.json('examples/people.json');

df.show();