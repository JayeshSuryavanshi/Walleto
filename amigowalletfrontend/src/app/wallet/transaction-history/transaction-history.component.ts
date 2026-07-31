import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { animate, keyframes, style, transition, trigger } from '@angular/animations';

import { UserTransaction } from '../../shared/model/user-transaction';
import { TransactionHistoryService } from './transaction-history.service';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css'],
  animations: [
    trigger('loadAnimation', [
      transition('void => *', [
        animate(
          '1000ms ease-out',
          keyframes([style({ opacity: 0, offset: 0 }), style({ opacity: 1, offset: 1 })]),
        ),
      ]),
    ]),
  ],
})
export class TransactionHistoryComponent implements OnInit {
  private readonly transactionHistoryService = inject(TransactionHistoryService);

  state = 'active';
  userTransactions: UserTransaction[] = [];
  errorMessage: string | null = null;
  page = 1;
  readonly pageSize = 5;
  sortoption = 'transactionDateTimeRev';
  filteroption = '';

  ngOnInit(): void {
    this.errorMessage = null;
    this.transactionHistoryService.getAllTransactions().subscribe({
      next: (transactions) => (this.userTransactions = transactions),
      error: (error) => (this.errorMessage = error?.error?.message ?? 'No transactions found'),
    });
  }

  onOptionChange(): void {
    this.page = 1;
  }

  private get processed(): UserTransaction[] {
    const filtered =
      this.filteroption === ''
        ? [...this.userTransactions]
        : this.userTransactions.filter((t) => t.paymentType?.paymentType === this.filteroption);

    return filtered.sort((a, b) => {
      switch (this.sortoption) {
        case 'userTransactionId':
          return a.userTransactionId - b.userTransactionId;
        case 'userTransactionIdRev':
          return b.userTransactionId - a.userTransactionId;
        case 'transactionDateTime':
          return new Date(a.transactionDateTime).getTime() - new Date(b.transactionDateTime).getTime();
        case 'transactionDateTimeRev':
        default:
          return new Date(b.transactionDateTime).getTime() - new Date(a.transactionDateTime).getTime();
      }
    });
  }

  get totalPages(): number {
    return Math.max(1, Math.ceil(this.processed.length / this.pageSize));
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  get pagedTransactions(): UserTransaction[] {
    const start = (this.page - 1) * this.pageSize;
    return this.processed.slice(start, start + this.pageSize);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.page = page;
    }
  }
}
