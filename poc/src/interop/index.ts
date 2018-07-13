import {readFileSync} from 'fs';
import java from 'java';

readFileSync('./classpath', 'utf8')
  .split(/:|;/)
  .forEach(jar => java.classpath.push(jar));
