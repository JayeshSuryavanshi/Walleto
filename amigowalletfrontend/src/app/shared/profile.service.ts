import { Injectable } from '@angular/core';
import { UriService } from './uri.service';
import { User } from './model/user';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * This service is used across the components to update the balance and points
 * as and when transactions are done.
 * 
 * @Injectable makes it as a service class and it can be injected where ever required
 */
@Injectable()
export class ProfileService {


    private balance: number;
    private points: number;
    private amigoWalletUri: string;

    constructor(private http:HttpClient, private uriService: UriService) {
        this.amigoWalletUri = this.uriService.buildAmigoWalletUri();
    }

    /** adds money to the current balance */
    addMoney(value: number) {
        this.balance = this.balance + value;
    }

    /** deducts money to the current balance */
    subMoney(value: number) {
        this.balance = this.balance - value;
    }

    /** get the current balance */
    getBalance() {
        return this.balance;
    }

    /** set the current balance */
    setBalance(value: number) {
        this.balance = value;
    }

    /** adds money to the current reward points */
    addPoints(value: number) {
        this.points = this.points + value;
    }

    /** deducts money to the current points */
    subPoints(value: number) {
        this.points = this.points - value;
    }

    /** get the current points */
    getPoints() {
        return this.points;
    }

    /** set the current points */
    setPoints(value: number) {
        this.points = value;
    }

    getBalanceFromDB(user: User): Observable<any> {
        return this.http.post<User>(this.amigoWalletUri + '/UserLoginAPI/getUser', user)
    }
      

}