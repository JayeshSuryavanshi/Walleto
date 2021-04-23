import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Bank } from "../../shared/model/bank";
import { Card } from '../../shared/model/card';
import { UserTransaction } from '../../shared/model/user-transaction';
import { UriService } from '../../shared/uri.service';
import { Observable } from 'rxjs';



/**
 * This is a service class used from LoadMoney component
 *  
 * this communicats with the server side application and does requied work
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 */
@Injectable()
export class LoadMoneyService {

  /** required url */
  amigoWalletUrl: string;
  eduBankUrl: string;

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   *
   * urls are initialized
   */
  constructor(private http: HttpClient, private uriService: UriService) {
    this.amigoWalletUrl = this.uriService.buildAmigoWalletUri();
    this.eduBankUrl = this.uriService.buildEduBankUri();
  }

/**
 * This method calls the cardPayment method
 * in DebitCardAPI of PaymentServices
 * using an http post request
 * which returns a ResponseEntity<String>
 */
  cardPayment(amount: number, data: any): Observable<any> {

    return this.http.post(this.eduBankUrl + '/DebitCardAPI/cardPayment/' + amount, data, {responseType: "text"})
  }
  
  /**
   * This method calls the loadMoneyDebitCard method
   * in DebitCardAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<UserTransaction>
   */
  loadMoneyDebitCard(amount: number, data: any): Observable<any> {

    return this.http.post<UserTransaction>(this.amigoWalletUrl + '/DebitCardAPI/loadMoneyDebitCard/' + amount, data)
  }
  
  /**
   * This method calls the deleteCard method
   * in DebitCardAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<Card>
   */
  deleteCard(data: any): Observable<any> {

    return this.http.post<Card>(this.amigoWalletUrl + '/DebitCardAPI/deleteCard', data)
  }

  /**
   * This method calls the addCard method
   * in DebitCardAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<Card>
   */
  addCard(userId: number, data: any): Observable<any> {

    return this.http.post<Card>(this.amigoWalletUrl + '/DebitCardAPI/addCard/' + userId, data)

  }

  /**
   * This method calls the cardVerification method
   * in DebitCardAPI of PaymentServices
   * using an http post request 
   * which returns a ResponseEntity<String>
   */
  cardVerification(data: any): Observable<any> {

    return this.http.post(this.eduBankUrl + '/DebitCardAPI/cardVerification', data, {responseType: "text"})
     
  }

  /**
   * This method calls the loadMoneyNetBanking method
   * in NetBankingAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<UserTransaction>
   */
  netBanking(data: any): Observable<any> {

    return this.http.post<UserTransaction>(this.amigoWalletUrl + '/NetBankingAPI/loadMoneyNetBanking', data)
  
  }

  /**
   * This method calls the fetch all banks method 
   * in DebitCardAPI of PaymentServices
   * using an http get request 
   * which returns a ResponseEntity<Bank[]>
   */
  getAllBanks(): Observable<any> {

    return this.http.get<Bank[]>(this.amigoWalletUrl + '/DebitCardAPI/fetchBankDetails')
  
  }
}