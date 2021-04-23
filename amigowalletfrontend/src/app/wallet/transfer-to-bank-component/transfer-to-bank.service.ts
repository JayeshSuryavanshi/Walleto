import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UriService } from 'src/app/shared/uri.service';

@Injectable({
  providedIn: 'root'
})
export class TransferToBankService {

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
   * This method calls the accountVerification method
   * in AccountAPI of PaymentServices
   * using an http post request
   * which returns a ResponseEntity<String>
   */
  accountVerification(data: any): Observable<any> {
    return this.http.post(this.eduBankUrl + '/AccountAPI/accountVerification', data, { responseType: 'text' })
  }

  /**
   * This method calls the creditMoney method
   * in AccountAPI of PaymentServices
   * using an http post request
   * which returns a ResponseEntity<String>
   */
  creditMoney(amount: number, data: any): Observable<any> {
    return this.http.post(this.eduBankUrl + '/AccountAPI/creditMoney/' + amount, data, { responseType: 'text' })
  }

  /**
   * This method calls the payToBank method
   * in PayToBankAPI of eWallet
   * using an http post request 
   * which returns a ResponseEntity<String>
   */
  sendToBank(data: any, amount: any): Observable<any> {
    return this.http.post(this.amigoWalletUrl + '/BankTrasnferAPI/sendMoneyBankAccount/' + amount, data, { responseType: 'text' })
  }
}
