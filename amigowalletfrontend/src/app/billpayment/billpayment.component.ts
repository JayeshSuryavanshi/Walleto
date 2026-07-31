import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { trigger, transition, keyframes, style, animate } from '@angular/animations';
import { TranslateModule } from '@ngx-translate/core';

import { AuthService } from '../shared/auth.service';
import { LoggerService } from '../shared/logger.service';
import { BillpaymentserviceService } from './billpaymentservice.service';

@Component({
  selector: 'app-billpayment',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './billpayment.component.html',
  styleUrls: ['./billpayment.component.css'],
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
export class BillpaymentComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly payBillService = inject(BillpaymentserviceService);
  private readonly auth = inject(AuthService);
  private readonly logger = inject(LoggerService);

  successMessage: string | null = null;
  message: string | null = null;
  amount = 0;
  num1 = 0;
  selectedMerchant = '';
  selectedMerchantType = '';
  merchants: string[] = [];
  services: string[] = [];
  submitted = false;

  form1 = this.fb.group({
    servicetype: ['', Validators.required],
    merchantname: ['', Validators.required],
  });

  ngOnInit(): void {
    this.amount = 0;
    this.num1 = 0;
    this.payBillService.displayServiceType().subscribe({
      next: (services) => {
        this.services = services;
        this.logger.info('Loading services success');
      },
      error: (error) => {
        this.message = error?.error?.message ?? null;
        this.logger.error('Loading services error', error);
      },
    });
  }

  displayName(type: string | null): void {
    const serviceType = type ?? '';
    this.num1 = 1;
    this.amount = 0;
    this.submitted = false;
    this.message = null;
    this.successMessage = null;
    this.selectedMerchantType = serviceType;
    this.payBillService.displayMerchantName(serviceType).subscribe({
      next: (merchants) => {
        this.merchants = merchants;
        this.logger.info('Loading merchants success');
      },
      error: (error) => {
        this.message = error?.error?.message ?? null;
        this.logger.error('Loading merchants error', error);
      },
    });
  }

  showBill(name: string | null): void {
    this.num1 = 1;
    this.submitted = false;
    this.message = null;
    this.successMessage = null;
    this.amount = Math.floor(Math.random() * 50 + 150);
    this.selectedMerchant = name ?? '';
  }

  payBill(): void {
    this.submitted = true;
    this.message = null;
    this.successMessage = null;
    this.payBillService.payBill(this.amount, this.selectedMerchant).subscribe({
      next: (response) => {
        this.successMessage = response?.message ?? 'Transaction successful';
        this.auth.refreshProfile().subscribe({ error: () => undefined });
        this.form1.controls.servicetype.setValue('');
        this.form1.controls.merchantname.setValue('');
        this.amount = 0;
        this.submitted = false;
        this.logger.info('Successful payment');
      },
      error: (error) => {
        this.submitted = false;
        this.message = error?.error?.message ?? error?.error ?? 'Payment failed';
        this.logger.error('Bill payment error', error);
      },
    });
  }
}
