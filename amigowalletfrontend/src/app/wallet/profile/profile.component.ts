import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../../shared/model/user';

import { ProfileService } from '../../shared/profile.service';

// This component displays the user information and links for change passsword and logout

/** Annotation which specifes this as a component 
 * 
 * selector: defines a tag which can be used in any html as <aw-profile></aw-profile>
 * moduleId: This specified for specifing that the path used in the component are relative to this component
 * templateUrl: This is the relative path for the html related to this component
 * styleUrls: This is the relative path for the css related to this component
*/
@Component({
  selector: 'aw-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

  idOfIntervalFunction: any;
  name: string;

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   */
  constructor(private _router: Router, public profileService: ProfileService) {
  }

  /**
   * Life cycle method on init (overided from OnInit)
   * 
   * Taking the user from session and set the value of balance 
   * and rewardPoints to the common profile service 
   */
  ngOnInit() {
    let user: User = JSON.parse(sessionStorage.getItem('user'));
    if (user != null) {
      this.name = user.name;
      this.profileService.setBalance(user.balance);
      this.profileService.setPoints(user.rewardPoints);
    }

    this.idOfIntervalFunction = setInterval(()=>{
      let tempUser = new User();
      tempUser.userId = user.userId;
      this.profileService.getBalanceFromDB(tempUser).subscribe(responseData => {
        let userFromBackEnd:User = responseData;
        this.profileService.setBalance(userFromBackEnd.balance);
        this.profileService.setPoints(userFromBackEnd.rewardPoints);
      })
    }, 20000)

  }

  /**
   * This method navigates to the changePassword page
   */
  changePassword() {
    this._router.navigate(['\changePassword']);
  }

  /**
   * This method clears the data in the sessionStorage
   * It then navigates to the login page
   */
  logOut() {
    sessionStorage.clear();
    this._router.navigate(['\login']);
    this.profileService.setBalance(0);
  }

  
  ngOnDestroy(): void {
    clearInterval(this.idOfIntervalFunction);
  }
}