import { Injectable } from '@angular/core';
import { Log } from 'ng2-logger/browser';

const LOG_CONFIG: any = {
    error: 'red',
    warn: 'yellow',
    info: 'blue',
};

const log = Log.create("Amigo Wallet");

/*
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
export class LoggerService {

   error(message: string, obj?: any) {
        log.color = LOG_CONFIG.error;
        (obj) ? log.error(message, obj) : log.error(message);
   }

   warn(message: string, obj?: any) {
        log.color = LOG_CONFIG.warn;
        (obj) ? log.warn(message, obj) : log.warn(message);
   }

   info(message: string, obj?: any) {
        log.color = LOG_CONFIG.info;
        (obj) ? log.info(message, obj) : log.info(message);
   }

}