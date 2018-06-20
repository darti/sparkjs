import fs from 'fs';
import Project from 'ts-simple-ast';

const project = new Project();

fs.readFileSync('generated/definitions.json');