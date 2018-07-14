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

  

java.classpath.push('/Users/matthieu/Projects/sparkjs/lambda-support/target/scala-2.11/classes');
java.classpath.push('Users/matthieu/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.12.jar');

const LambdaJs = java.import('io.ekai.sparkjs.lambda.LambdaJs');

java.getStaticFieldValue('io.ekai.sparkjs.lambda.LambdaJs', 'engine')
