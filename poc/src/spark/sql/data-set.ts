import { Wrapper } from '../../interop';

export class Dataset<T> extends Wrapper {

    public show(): void {
        this.callSync('show');
    }
}