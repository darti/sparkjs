import { Wrapper } from '../../interop';

export class Row extends Wrapper {
    public getString(i: number) : string {
        return this.callSync('getString', i);
    }
}