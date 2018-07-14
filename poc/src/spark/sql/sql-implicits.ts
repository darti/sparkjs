import { Wrapper } from '../../interop';
import { Encoder } from './encoder';

export class SQLImplicits extends Wrapper {
    public newStringEncoderSync(): Encoder<string> {
        return new Encoder(this.callSync('newStringEncoderSync'));
    }
}