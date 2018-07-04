export interface TypeDef {
    name: string;
    path: string[];
    args: string[];
}

export interface ParameterDef {
    name: string;
    typ: TypeDef;
}

export interface MethodDef {
    name: string;
    static: boolean;
    returnType: TypeDef;
    parameters: ParameterDef[];
}

export interface ClassDef {
    typ: TypeDef;
    static: boolean;
    methods: MethodDef[];
    innerClasses: ClassDef[];
}
