import {readFileSync} from 'fs';
import {join} from 'path';
import {ClassDef} from './generator-model';

import del from 'del';
import { Generator } from './generator';

const root = 'lib/generated';

del(root);

const definitions = JSON.parse(
  readFileSync('lib/definitions/definitions.json', 'utf8')
) as ClassDef[];


const generator = new Generator(join('lib', 'tsconfig.json'));
generator.generate(root, definitions);

console.log('Done');
