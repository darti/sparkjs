import { readFileSync } from 'fs';
import java from 'java';
import winston from 'winston';
import { format } from 'winston';

export const logger = winston.createLogger({
  format: format.combine(
    format.splat(), 
    format.cli()
  ),
  level: 'info',
  transports: [new winston.transports.Console()]
});

logger.info('Initializing classpath');

readFileSync('classpath', 'utf8')
  .split(/:|;/)
  .forEach(jar => {
    logger.debug('Add jar to classpath: %s', jar);
    java.classpath.push(jar);
  });

export class Wrapper {
  // public static callStatic(methodName: string, ...args: any[]) : any {
  //     return java.callStaticMethodSync(this., methodName, ...args);
  // }

  constructor(private javaObject: any) {}

  public get asJava(): any {
    return this.javaObject;
  }

  public callSync(methodName: string, ...args: any[]): any {
    return java.callMethodSync(this.javaObject, methodName, ...args);
  }
}
