import { Wrapper } from '../../interop';
import { SparkSession } from './spark-session';
import java from 'java';

export class Dataset<T> extends Wrapper {
  public show(): void {
    this.callSync('show');
  }

  public get sparkSession(): SparkSession {
    return new SparkSession(this.callSync('sparkSession'));
  }

  public map<U>(f: (t: T) => U): Dataset<U> {
   // return new Dataset(this.callSync('map', f, this.sparkSession.implicits.newStringEncoderSync().asJava));
   return new Dataset(this.callSync('map', f, java.callStaticMethodSync('org.apache.spark.sql.Encoders', 'STRING')));
  }
}
