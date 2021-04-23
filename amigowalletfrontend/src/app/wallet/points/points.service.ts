import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../shared/model/user';
import { UriService } from '../../shared/uri.service';



/**
 * This is a service class used from Point component
 * 
 * this communicats with the server side application and does requied work
 *
 * @Injectable makes it as a service class and it can be injected where ever required
 */
@Injectable()
export class PointsService {

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
   * This method calls the redeemRewardPoints method
   * in RewardPointsAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<User>
   */
  redeem(data: any): Observable<any> {

    return this.http.post<User>(this.amigoWalletUrl + '/RewardPointsAPI/redeemRewardPoints', data)
  }
}