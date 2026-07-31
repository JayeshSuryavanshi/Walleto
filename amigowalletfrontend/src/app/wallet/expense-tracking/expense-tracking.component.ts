import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserTransaction } from '../../shared/model/user-transaction';
import { TransactionHistoryService } from '../transaction-history/transaction-history.service';

interface SpendingAccumulator {
  thisMonth: number;
  lastMonth: number;
  lastDateTime: Date | null;
}

@Component({
  selector: 'app-expense-tracking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './expense-tracking.component.html',
  styleUrls: ['./expense-tracking.component.css'],
})
export class ExpenseTrackingComponent implements OnInit {
  private readonly transactionHistoryService = inject(TransactionHistoryService);

  errorMessage: string | null = null;
  moneySpentThisMonth = 0;
  projectedMonthlyExpenditure = 0;
  avgMonthlySpending = 0;

  ngOnInit(): void {
    this.transactionHistoryService.getAllTransactions().subscribe({
      next: (transactions) => this.compute(transactions),
      error: () => (this.errorMessage = 'Sorry, No data available'),
    });
  }

  private compute(userTransactions: UserTransaction[]): void {
    const today = new Date();

    const debits = userTransactions.filter((t) => t.paymentType?.paymentType === 'D');

    this.moneySpentThisMonth = debits
      .filter((t) => {
        const d = new Date(t.transactionDateTime);
        return d.getMonth() === today.getMonth() && d.getFullYear() === today.getFullYear();
      })
      .reduce((acc, t) => acc + t.amount, 0);

    const numberOfDaysThisMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0).getDate();
    this.projectedMonthlyExpenditure = this.moneySpentThisMonth * (numberOfDaysThisMonth / today.getDate());

    const sortedDebits = [...debits].sort(
      (a, b) => new Date(a.transactionDateTime).getTime() - new Date(b.transactionDateTime).getTime(),
    );

    const calculation = sortedDebits.reduce<SpendingAccumulator>(
      (acc, transaction) => {
        const transactionDate = new Date(transaction.transactionDateTime);
        if (acc.lastDateTime == null) {
          acc.lastDateTime = transactionDate;
          acc.thisMonth = transaction.amount;
          return acc;
        }
        if (
          !(
            transactionDate.getMonth() === acc.lastDateTime.getMonth() &&
            transactionDate.getFullYear() === acc.lastDateTime.getFullYear()
          )
        ) {
          acc.lastMonth = (acc.lastMonth + acc.thisMonth) / 2;
          acc.thisMonth = 0;
        }
        acc.thisMonth += transaction.amount;
        acc.lastDateTime = transactionDate;
        return acc;
      },
      { thisMonth: 0, lastMonth: 0, lastDateTime: null },
    );

    if (
      calculation.lastDateTime != null &&
      calculation.lastDateTime.getFullYear() === today.getFullYear() &&
      calculation.lastDateTime.getMonth() === today.getMonth()
    ) {
      this.avgMonthlySpending = calculation.lastMonth;
    } else {
      this.avgMonthlySpending = (calculation.lastMonth + calculation.thisMonth) / 2;
    }
  }
}
