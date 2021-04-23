import { Injectable } from '@angular/core';

import { UriService } from '../../shared/uri.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * This is a service class used from walletToWalletTransfer component
 * 
 * this communicats with the server side application and does requied work
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 */
@Injectable()
export class LoginService {

  /** required url */
  amigoWalletUrl: string;

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   *
   * urls are initialized
   */
  constructor(private http: HttpClient, private uriService: UriService) {
    this.amigoWalletUrl = this.uriService.buildAmigoWalletUri();
  }

  /**
   * This method calls the authenticate method
   * in UserLoginAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<User>
   */
  doLogin(data: any): Observable<any> {

    return this.http.post(this.amigoWalletUrl + '/UserLoginAPI/authenticate', data)
  }
}
