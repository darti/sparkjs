import { DataFrame } from '.';
import { Wrapper } from '../../interop';
import { Dataset } from './dataset';

export class DataFrameReader extends Wrapper {

    public json(args: Dataset<string> |  string[] | string): DataFrame {
        return new DataFrame(this.callSync('json', args));
    }
}
