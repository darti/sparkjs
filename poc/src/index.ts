import { SparkSession } from './spark/sql/spark-session';

const b = SparkSession.builder()
  .master('local')
  .appName('SparkJS');


  b.getOrCreate();
