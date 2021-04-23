import { Component, OnInit } from '@angular/core';
import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { TransactionHistoryService } from './transaction-history.service';
import { UserTransaction } from 'src/app/shared/model/user-transaction';
import { User } from 'src/app/shared/model/user';

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css'],
  providers: [TransactionHistoryService],
  animations: [
    /** trigger the animation when loadAnimation (an attribute in div tag of Html)
    * value has a transition from void to some value(*) ( void when the component is not at loaded,
    * once the component is loaded it will be initialized to active)
    */
    trigger('loadAnimation', [
      transition('void => *', [
        animate("1000ms ease-out", keyframes([  // key frame specifies the set of styles which should be applied based on timeline
          style({ opacity: 0, offset: 0 }),     // at 0th (offset * time) milisecond opacity is 0
          style({ opacity: 1, offset: 1 }),     // at 1000th (offset * time) milisecond opacity is 1
        ]))
      ])
    ])
  ]
})
export class TransactionHistoryComponent implements OnInit {

  userTransactions: UserTransaction[] = [];
  errorMessage:String;
  page:number;
  sortoption:string="transactionDateTimeRev";
  filteroption:string="";

  constructor(private transactionHistoryService:TransactionHistoryService) { }

  ngOnInit() {
    this.errorMessage = null;
    let user: User = JSON.parse(sessionStorage.getItem("user"));
    this.transactionHistoryService.getAllTransactions(user).subscribe(
      (responseData) => {this.userTransactions = responseData},
      (error) => {this.errorMessage = error.error.message;}
    );
    this.page = 1;
  }

  onPageChange(event){
    this.page = event;
  }
}
