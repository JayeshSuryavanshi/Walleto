import { Injectable } from '@angular/core';
import { SecurityQuestion } from '../../shared/model/security-question';
import { UriService } from '../../shared/uri.service';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class SecurityQuestionService {
  
  amigoWalletUrl: string;

  constructor(private http:HttpClient, private uriService:UriService) {
    this.amigoWalletUrl = this.uriService.buildAmigoWalletUri();
   }

  getAllQuestions():Observable<SecurityQuestion[]>{
    return this.http.get<SecurityQuestion[]>(this.amigoWalletUrl + "/RegistrationAPI/getAllQuestions")
  }

  register(data: any): Observable<string> {
    return this.http.post(this.amigoWalletUrl + "/RegistrationAPI/register",data, {responseType: "text"})
  }

}
