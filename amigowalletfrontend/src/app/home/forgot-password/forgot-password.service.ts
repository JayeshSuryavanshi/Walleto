import { Injectable } from '@angular/core';

import { UriService } from '../../shared/uri.service';
import { User } from '../../shared/model/user';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * This is a service class used from ForgotPassword component
 * 
 * this communicats with the server side application and does requied work
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 */
@Injectable()
export class ForgotPasswordService {

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
   * This method calls the forgotPasswordSendMail method
   * in ForgotPasswordAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<String>
   */
  forgotPassword(data: any): Observable<User> {

    return this.http.post<User>(this.amigoWalletUrl + '/ForgotPasswordAPI/forgotPassword', data)

  }


  checkAnswer(user: User): Observable<string> {
    return this.http.post(this.amigoWalletUrl + '/ForgotPasswordAPI/validateAnswer', user, {responseType: "text"})
  }

  /**
 * This method calls the resetPassword method
 * in ForgotPasswordAPI of eWallet
 * using an http post request 
 * which returns a ResponseEntity<String>
 */
  resetPasswordPost(data: any): Observable<string> {
    return this.http.post(this.amigoWalletUrl + '/ForgotPasswordAPI/resetPassword/', data, {responseType: "text"})
  }
}