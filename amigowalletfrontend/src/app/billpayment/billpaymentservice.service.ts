import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UriService } from '../shared/uri.service';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class BillpaymentserviceService {
  amigoWalletUrl: string;
  constructor(private http: HttpClient, private uriService: UriService) { 
    this.amigoWalletUrl=this.uriService.buildAmigoWalletUri();
  }

  displayServiceType():Observable<String[]> {
    return this.http.get<String[]>(this.amigoWalletUrl + '/WalletToMerchantTransferAPI/serviceType')

  }

  displayMerchantName(data:String): Observable<String[]>{
    return this.http.post<String[]>(this.amigoWalletUrl + '/WalletToMerchantTransferAPI/merchantType',data)
  }

  payBill(userId:number,amount:number,data:any) {
    return this.http.post(this.amigoWalletUrl + '/WalletToMerchantTransferAPI/payBill/'+amount+ '/' + userId,data, {responseType:'text'})
  } 

}
