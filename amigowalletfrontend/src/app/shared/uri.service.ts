import { Injectable } from '@angular/core';

/**
 * This is a service class used across all the components
 * 
 * this provides the url for server side applications to all the 
 * http calls in the service classes
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 * 
 * Even those service that don't have dependencies do not technically
 * require it. But it good to use it because:
 *      Future proofing: No need to remember @Injectable() when you add a 
 *          dependency later.
 *      Consistency: All services follow the same rules, and you don't have
 *          to wonder why a decorator is missing.
 */
@Injectable()
export class UriService {

    /** edubank server properties */
    eduBankUri = {
        protocol: 'http',
        // host:  'vjeemys-19',
        host:  'localhost',
        port: '3331',
        applicationName: 'EDUBank'
    };

    /** ewallet server properties */
    amigoWalletUri = {
        protocol: 'http',
        // host:  'vjeemys-19',
        host:  'localhost',
        port: '3322',
        applicationName: 'AmigoWallet'
    };

    /**
     * This function builds the uri required for accessing the EDUBank application
     */
    buildEduBankUri() {
        return this.eduBankUri.protocol + "://" + this.eduBankUri.host + ":" + this.eduBankUri.port + "/" + this.eduBankUri.applicationName;
    }

    /**
     * This function builds the uri required for accessing the e-Wallet application
     */
    buildAmigoWalletUri() {
        return this.amigoWalletUri.protocol + "://" + this.amigoWalletUri.host + ":" + this.amigoWalletUri.port + "/" + this.amigoWalletUri.applicationName;
    }


}