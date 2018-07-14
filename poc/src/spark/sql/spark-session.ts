import java from 'java';
import { Wrapper } from '../../interop';

class Builder extends Wrapper {
  public master(master: string): Builder {
    return new Builder(this.callSync('master', master));
  }

  public appName(name: string): Builder {
    return new Builder(this.callSync('appName', name));
  }

  public getOrCreate(): SparkSession {
    return new SparkSession(this.callSync('getOrCreate'));
  }
}

export class SparkSession extends Wrapper {
  get [Symbol.toStringTag]() {
    return 'org.apache.spark.sql.SparkSession';
  }

  public static builder(): Builder {
    return new Builder(
      java.callStaticMethodSync('org.apache.spark.sql.SparkSession', 'builder')
    );
  }
}
