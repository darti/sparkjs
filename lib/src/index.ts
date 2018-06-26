import {readFileSync} from 'fs';
import Project from 'ts-simple-ast';
import {join}from 'path';
import {ClassDef} from './generator-model';

import del from 'del';

const root = 'lib/generated';

del(root);

const project = new Project({
    tsConfigFilePath: 'lib/tsconfig.json'
});

const definitions = JSON.parse(
  readFileSync('lib/definitions/definitions.json', 'utf8')
) as ClassDef[];


const wrapper = project.getSourceFile('wrapper.ts');


definitions.forEach(cd => {
  const output = join(root, ...cd.typ.path, `${cd.typ.name}.ts`);
  const src = project.createSourceFile(output, '', { overwrite: true });

  const cls = src.addClass({
    name: cd.typ.name,
    isExported: true, 
    extends: 'Wrapper'
  });

  src.addImportDeclaration({
    namedImports: ['Wrapper'],
    moduleSpecifier: src.getRelativePathAsModuleSpecifierTo(wrapper),

  })

  src.saveSync();
});
