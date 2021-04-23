import { Component, OnInit } from '@angular/core';
import { TransactionHistoryService } from '../transaction-history/transaction-history.service';
import { User } from 'src/app/shared/model/user';

@Component({
  selector: 'app-expense-tracking',
  templateUrl: './expense-tracking.component.html',
  styleUrls: ['./expense-tracking.component.css']
})
export class ExpenseTrackingComponent implements OnInit {

  errorMessage: string;
  moneySpentThisMonth: number;
  projectedMonthlyExpenditure: number;
  avgMonthlySpending: number;
  projected: number;
  constructor(
    private transactionHistoryService: TransactionHistoryService
  ) { }

  ngOnInit() {
    let user: User = JSON.parse(sessionStorage.getItem("user"));
    this.transactionHistoryService.getAllTransactions(user).subscribe(
      (resp) => {
        let userTransactions: any[] = resp;
        const today = new Date();

        let moneySpentThisMonth: number = 0;

        this.moneySpentThisMonth = userTransactions.filter(
          (transaction) => {
            const transactionDate = new Date(transaction.transactionDateTime);
            return transactionDate.getMonth() == today.getMonth() 
              && transactionDate.getFullYear() == today.getFullYear() 
          }
        )
        .filter(
          (transaction) => {
            return transaction.paymentType.paymentType == "D";
          }
        ).reduce(
          (acc: number, transactionObj: any) => {
            return acc + transactionObj.amount;
          },
          0
        );
        
        const numberOfDaysThisMonth = new Date(today.getFullYear(), today.getMonth()+1, 0).getDate();
        this.projectedMonthlyExpenditure = this.moneySpentThisMonth * (numberOfDaysThisMonth/today.getDate());

        let calculation:any = userTransactions.filter(
          (transaction) => transaction.paymentType.paymentType == "D"
        ).sort(
          (trn1, trn2) => {
            if(new Date(trn1.transactionDateTime) < new Date(trn2.transactionDateTime)){
              return -1;
            }
            return 1;
          }
        ).reduce(
          (acc, transaction) => {

            let transactionDate = new Date(transaction.transactionDateTime);
            
            if(acc.lastDateTime == null){
              acc.lastDateTime = transactionDate;
              acc.thisMonth = transaction.amount;
              return acc;
            }

            if(!(transactionDate.getMonth() == acc.lastDateTime.getMonth() && transactionDate.getFullYear() == acc.lastDateTime.getFullYear())) {
              acc.lastMonth = (acc.lastMonth+acc.thisMonth)/2;
              acc.thisMonth = 0;
            }
            acc.thisMonth += transaction.amount;
            acc.lastDateTime = transactionDate;

            return acc;
          }
          , {
            thisMonth: 0,
            lastMonth: 0,
            lastDateTime: null
          }
        );

        if(calculation.lastDateTime.getFullYear() == today.getFullYear() && calculation.lastDateTime.getMonth() == today.getMonth()){
          this.avgMonthlySpending = calculation.lastMonth;
        }
        else {
          this.avgMonthlySpending = (calculation.lastMonth+calculation.thisMonth)/2;
        }

      },
      (error) => {this.errorMessage = "Sorry, No data available"}
    );
  }

}
