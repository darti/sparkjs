import java from 'java';

export class Wrapper {
    constructor(private javaObject: any) {}

    public get asJava(): any {
        return this.javaObject;
    }

    public callSync(methodName: string, ...args: any[]): any {
        return java.callMethodSync(this.javaObject, methodName, ...args);
    }
}
