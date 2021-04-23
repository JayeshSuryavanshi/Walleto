import { Component, OnInit } from '@angular/core';
import { BillpaymentserviceService } from './billpaymentservice.service';
import { trigger, transition, keyframes, style, animate } from '@angular/animations';
import { FormBuilder, Validators } from '@angular/forms';
import { LoggerService } from '../shared/logger.service';
import { ProfileService } from '../shared/profile.service';
import { TranslateService } from '@ngx-translate/core';
import { error } from 'util';
import { User } from '../shared/model/user';

@Component({
  selector: 'app-billpayment',
  templateUrl: './billpayment.component.html',
  styleUrls: ['./billpayment.component.css'],
  providers:[BillpaymentserviceService],
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
export class BillpaymentComponent implements OnInit {

  state = "active";
  successMessage: any;
  message: any;
  error: any;
  amount: number=0;
  num1: number;
  selectedMerchant: String;
  merchants: String[];
  services: String[];
  submitted = false;
  selectedMerchantType:String;


  constructor(public fb: FormBuilder, private payBillService: BillpaymentserviceService,
    private profileService: ProfileService, private logger: LoggerService,
    private translateService: TranslateService
) { }

  ngOnInit() {
    this.amount=0;
    this.num1=0;
    this.payBillService.displayServiceType().subscribe(
      (responseData: any) => {
        this.services=responseData;
        this.logger.info("Loading merchants success");

      },
     
      error=>{ this.message = error.errorMessage;
        this.logger.error("Loading merchants error: " + this.message, error);

      }
    )
  }

  form1 = this.fb.group({
    servicetype: ["", Validators.required],
    merchantname: ["", Validators.required],
  });




  displayName(type:String)

  { this.num1=1;
    this.amount=0;
    this.submitted = false;
    this.message = null;
    this.successMessage = null;
   
    this.selectedMerchantType=type;
    this.payBillService.displayMerchantName(this.selectedMerchantType).subscribe(
      (responseData: any)=>{
        this.merchants=responseData;
        this.logger.info("Loading merchants success");

      },
      error=>{ this.message = error.errorMessage;
        console.log(error);
        
        this.logger.error("Loading merchants error: " + this.message, error);
        
      }
    )
 }

 showBill(name:String){
  this.num1=1;
  this.submitted = false;
  this.message = null;
  this.successMessage = null;
  this.amount=Math.floor(Math.random()*50+150);
  this.selectedMerchant=name;


 }

 payBill(){
  this.submitted = true;
  this.message = null;
  this.successMessage = null;
  let user: User = JSON.parse(sessionStorage.getItem("user"));
  this.payBillService.payBill(user.userId,this.amount,this.selectedMerchant).subscribe((responseData: any) => {

    
  console.log(responseData)
  this.successMessage = responseData;
  this.profileService.subMoney(this.amount);
  this.profileService.addPoints(Math.floor(Math.round(this.amount) / 10));

  this.form1.controls['servicetype'].setValue("");
  this.form1.controls['merchantname'].setValue("");
  this.amount = null;
  this.submitted = false;
  this.logger.info("Successfull payment");
},
  error => {
    // // error.error = JSON.parse(error.error);
    // if (error.error.message != null) {
    //   this.message = error.error.message;
    // } else {
    //   this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.message = value)
    //     ;
    // }
    // this.submitted = false;
    // this.logger.error(this.message, error);    
    console.log(error);
    console.log(typeof(error.error))
    this.submitted = false;
    try{
      const errorObj = JSON.parse(error.error);
      this.message = errorObj.message;
      return;
    }
    catch(e){
    this.message = error.error;
    }
    this.logger.error("Loading merchants error: " + this.message, error);
  });

 }



 

}
