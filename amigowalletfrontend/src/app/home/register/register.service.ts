import { Injectable } from '@angular/core';

import { UriService } from '../../shared/uri.service';
import { SecurityQuestion } from '../../shared/model/security-question';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

/**
 * This is a service class used from Register component
 * 
 * this communicats with the server side application and does requied work
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 */
@Injectable()
export class RegisterService {

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
   * This method calls the validateForRegistration method
   * in RegistrationAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<User>
   */
  doRegister(data: any): Observable<any> {

    return this.http.post(this.amigoWalletUrl + '/RegistrationAPI/validateForRegistration', data)
  }

  getAllSecurityQuestions(): Observable<SecurityQuestion[]> {
    return this.http.get<SecurityQuestion[]>("assets/resources/securityQuestion.json")
  }

}
