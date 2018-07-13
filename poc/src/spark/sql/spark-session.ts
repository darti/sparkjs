import java from 'java';
import { Wrapper } from '../../interop/wrapper';

class Builder extends Wrapper {}

export class SparkSession extends Wrapper {
  get [Symbol.toStringTag]() {
    return 'org.apache.spark.sql.SparkSession';
  }

  public static builder(): Builder {
    return new Builder(java.callStaticMethodSync('org.apache.spark.sql.SparkSession', 'builder'));
  }
}
