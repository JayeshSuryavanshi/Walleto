import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UriService } from 'src/app/shared/uri.service';
import { Observable } from 'rxjs';
import { UserTransaction } from 'src/app/shared/model/user-transaction';

/**
 * This is a service class used from TransactionHistory component
 *  
 * this communicats with the server side application and does requied work
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 */
@Injectable({
  providedIn: 'root'
})
export class TransactionHistoryService {

  /** required url */
  amigoWalletUrl: string;

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   *
   * urls are initialized
   */
  constructor(private http:HttpClient, private uriService:UriService) {
    this.amigoWalletUrl = this.uriService.buildAmigoWalletUri();  
  }

  /**
   * This method calls the getAllTransactions method
   * in TransactionHistoryAPI of eWallet
   * using an http post request
   * which returns a ResponseEntity<UserTransaction[]>
   */
  getAllTransactions(data:any): Observable<any>{
    return this.http.post<UserTransaction[]>(this.amigoWalletUrl + '/TransactionHistoryAPI/getAllTransactions', data);
  }
}
