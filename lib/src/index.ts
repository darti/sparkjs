import {readFileSync} from 'fs';
import Project from 'ts-simple-ast';
import {join}from 'path';
import {ClassDef} from './generator-model';

const project = new Project({
    tsConfigFilePath: 'lib/tsconfig.json'
});

const root = 'lib/generated';

const definitions = JSON.parse(
  readFileSync('lib/generated/definitions.json', 'utf8')
) as ClassDef[];


const wrapper = project.getSourceFile('interop/wrapper.ts');


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
    moduleSpecifier: src.getRelativePathTo(wrapper)
  })

  src.saveSync();
});
