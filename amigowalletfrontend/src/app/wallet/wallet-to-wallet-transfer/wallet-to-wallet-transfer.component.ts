
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';




import { AmountValidator } from '../../shared/amount.validator';



import { WalletToWalletTransferService } from './wallet-to-wallet-transfer.service';
import { ProfileService } from 'src/app/shared/profile.service';

import { User } from 'src/app/shared/model/user';

import { TranslateService } from '@ngx-translate/core';
import { LoggerService } from 'src/app/shared/logger.service';




@Component({
  selector: 'app-wallet-to-wallet-transfer',
  templateUrl: './wallet-to-wallet-transfer.component.html',
  styleUrls: ['./wallet-to-wallet-transfer.component.css']
  
  
})
  

export class WalletToWalletTransferComponent implements OnInit {
  tranfertowallet:FormGroup;
  emailid:string;
  transferamount:number;
  successMessage:string;
  errorMessage:string;
  balance:number;
  amountToTransfer:number;

  constructor(private fb:FormBuilder,private service:WalletToWalletTransferService,private logger: LoggerService,
    public serviceProfile:ProfileService,private translateService: TranslateService) { }

  ngOnInit() {
    this.tranfertowallet=this.fb.group({
      emailid:['',[Validators.required,Validators.pattern("[^@]+[@][^@]+[.][^@]+")]],
      transferamount:['',[Validators.required,AmountValidator.min,Validators.pattern('^(\\d)*(.[\\d]{1,2})?$')]]
    });
  }

  transfer(){
    this.successMessage=null;
    this.errorMessage=null;
    
    
    let user: User = JSON.parse(sessionStorage.getItem('user'));
    if(user.emailId==this.tranfertowallet.get('emailid').value)
    
      this.translateService.get("ERROR_MESSAGES.SELF_WALLET_TRANSFER_ERROR").subscribe(value => this.errorMessage = value);
    else{
    this.serviceProfile.getBalanceFromDB(user).subscribe(
      responseData=>{let userFromBackEnd:User=responseData;
      this.balance=userFromBackEnd.balance
      
      this.serviceProfile.setPoints(userFromBackEnd.rewardPoints);
    
    if(this.balance<this.tranfertowallet.get('transferamount').value){
    
    let num=(Math.round(this.balance*100)/100).toFixed(2);
    this.translateService.get("ERROR_MESSAGES.TRANSFER_BANK_LOW_BALANCE",{value:num}).subscribe(value => this.errorMessage = value);
      }else{

    let userId=user.userId
    let amountToTransfer:number=this.tranfertowallet.get('transferamount').value
    let emailIdToTransfer=this.tranfertowallet.get('emailid').value
    let data:any[]=[userId,amountToTransfer,emailIdToTransfer]
    
    
    this.service.servicetransfer(data).subscribe(
      (response)=>{this.successMessage=response; this.logger.info("Transaction success")
      this.tranfertowallet.reset();},
      (error)=>{error.error=JSON.parse(error.error);console.log(error.error);this.errorMessage=error.error.message;console.log(this.errorMessage)}
    )
    
  }
})
}




  }}