import fs from 'fs';
import java from 'java';

fs
  .readFileSync('./classpath', 'utf8')
  .split(/:|;/)
  .forEach(jar => java.classpath.push(jar));
