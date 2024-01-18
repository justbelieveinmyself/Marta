import {Injectable} from '@angular/core';
import * as CryptoJS from 'crypto-js';
import {AES} from 'crypto-js';

@Injectable({
    providedIn: 'root'
})
export class EncryptionService {
    private SECRET_KEY = '123ADMINLOH321LADNONEBEITE';

    encryptData(data: string): string {
        let encrypted = AES.encrypt(data, this.SECRET_KEY);
        data = encrypted.toString();
        return data;
    }

    decryptData(data: string): string {
        let decrypted = AES.decrypt(data, this.SECRET_KEY);
        data = decrypted.toString(CryptoJS.enc.Utf8);
        return data;
    }
}
