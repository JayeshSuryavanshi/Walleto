import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UriService } from '../../shared/uri.service';

@Injectable({
  providedIn: 'root'
})
export class WalletToWalletTransferService {
  amigoWalletUrl: string;

  constructor(private http:HttpClient, private uriService: UriService) {}

  servicetransfer(data: any) : Observable<any> {
    return this.http.post(this.uriService.buildAmigoWalletUri()+"/WalletToWalletAPI/transfertowallet",data, {responseType: 'text'});
  }

}
