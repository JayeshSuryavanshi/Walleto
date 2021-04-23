import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { LoggerService } from '../../shared/logger.service';
import { User } from "../../shared/model/user";
import { ProfileService } from '../../shared/profile.service';
import { PointsService } from './points.service';

// This component used for redeem the reward points


/** Annotation which specifes this as a component 
 * 
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
 * providers: specifies the service class which communicates with the Serverside application
 *            specifing here makes it available only in this class
 * animations: specifing the animation for the component
*/
@Component({
  templateUrl: './points.component.html',
  styleUrls: ['./points.component.css'],
  providers: [PointsService],
  animations: [
    /** trigger the animation when loadAnimation (an attribute in div tag of Html)
     * value has a transition from void to some value(*) ( void when the component is not at loaded,
     * once the component is loaded it will be initialized to active)
     */
    trigger('loadAnimation', [
      transition('void => *', [
        animate("1000ms ease-out", keyframes([    // key frame specifies the set of styles which should be applied based on timeline
          style({ opacity: 0, offset: 0 }),       // at 0th (offset * time) milisecond opacity is 0
          style({ opacity: 1, offset: 1 }),       // at 1000th (offset * time) milisecond opacity is 1
        ]))
      ])
    ])
  ]
})
export class PointsComponent implements OnInit {

  /** class instance variales */
  state = "active";
  user: User;
  userId: number;
  successMessage: string;
  errorMessage: string;
  error: any;
  submitted = false;

  /**
    * form group for redeem the reward points 
    * 
    * Validation of userId   - required and setted from session
    *            
    */
  form = this.fb.group({
    userId: ["", Validators.required],
  });

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   */
  constructor(public fb: FormBuilder, private pointService: PointsService, private translateService: TranslateService,
    private profileService: ProfileService, private logger: LoggerService) { }

  /**
   * Life cycle method on init (overided from OnInit)
   * 
   * Taking the user from session for getting the userId
   */
  ngOnInit() {
    let user: User = JSON.parse(sessionStorage.getItem("user"));
    this.userId = user.userId;
    this.form.controls.userId.setValue(this.userId);
  }

  /**
   * This method calls redeem method of PointService
   * which returns a user model populated with appropriate message.
   * In case of success it displays a success message
   * In case of failure it displays an error message
   */
  redeem() {
    this.submitted = true;
    this.successMessage = null;
    this.errorMessage = null;
    this.pointService.redeem(this.form.value).subscribe((responseData: any) => {
      let user: User = responseData;
      this.profileService.addMoney(Number((this.profileService.getPoints() * 0.1)));
      this.profileService.setPoints(0);
      let user1: User = JSON.parse(sessionStorage.getItem("user"));
      user1.balance = this.profileService.getBalance();
      user1.rewardPoints = this.profileService.getPoints();
      sessionStorage.setItem("user", JSON.stringify(user1));
      this.successMessage = user.successMessage;
      this.submitted = false;
      this.logger.info(this.successMessage);
    }, error => {
      if (error.error.message != null) {
        this.errorMessage = error.error.message;
      } else {
        this.translateService.get("ERROR_MESSAGES.SERVER_DOWN").subscribe(value => this.errorMessage = value)
          ;
      }
      this.submitted = false;
      this.logger.error(this.errorMessage, error);
    });
  }
}