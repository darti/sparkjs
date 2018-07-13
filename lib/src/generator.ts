import { join } from 'path';
import {
  ClassDeclaration,
  ClassDeclarationStructure,
  SourceFile,
  Scope,
  ParameterDeclarationStructure
} from 'ts-simple-ast';
import Project from 'ts-simple-ast';
import { ClassDef, MethodDef, ParameterDef } from './generator-model';

export class Generator {
  public project: Project;
  public wrapper: SourceFile;

  constructor(tsConfigFile: string) {
    this.project = new Project({
      tsConfigFilePath: tsConfigFile
    });

    this.wrapper = this.project.getSourceFile('wrapper.ts');
  }

  public generate(root: string, definitions: ClassDef[]) {
    definitions.forEach(cd => {
      console.log(cd.typ.name);

      const output = join(root, ...cd.typ.path, `${cd.typ.name}.ts`);
      const src = this.project.createSourceFile(output, '', {
        overwrite: true
      });

      const cls = this.generateClass(src, cd);
      cd.innerClasses.forEach(icd => this.generateClass(src, icd));

     src.addImportDeclaration({
        moduleSpecifier: src.getRelativePathAsModuleSpecifierTo(this.wrapper),
        namedImports: ['Wrapper']
      });

      src.formatText();
      src.saveSync();
    });
  }

  private generateClass(src: SourceFile, cd: ClassDef): ClassDeclaration {
    const cls = src.addClass({
      extends: 'Wrapper',
      isExported: true,
      name: cd.typ.name
    });

    cd.methods.forEach(md => this.generateMethod(cls, md));

    return cls;
  }

  private generateMethod(cls: ClassDeclaration, md: MethodDef) {
    const method = cls.addMethod({
      isStatic: md.static,
      name: md.name,
      parameters: md.parameters.map(this.generateParameter),
      returnType: md.returnType.name,
      scope: Scope.Public
    });

    method.addBody().setBodyText(this.generateMethodCall(md));

    return method;
  }

  private generateMethodCall(md: MethodDef) {
    if (md.parameters.length === 0) {
      return `this.callSync("${md.name}");`;
    } else {
      return `return this.callSync("${md.name}", ${md.parameters
        .map(p => p.name)
        .reduceRight((acc, e) => `${acc}, ${e}`)});`;
    }
  }

  private generateParameter(p: ParameterDef): ParameterDeclarationStructure {
    return {
      name: p.name,
      type: p.typ.name
    };
  }
}

