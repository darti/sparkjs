import { SparkSession } from './spark/sql/spark-session';

const spark = SparkSession.builder()
  .master('local')
  .appName('SparkJS')
  .getOrCreate();

const df = spark.read.json('examples/people.json');

df.show();

const df2 = df.map(p => p.getString(1));

df2.show();