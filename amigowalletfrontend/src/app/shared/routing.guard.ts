import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from "./model/user";

/**
 * This class is used for authenticating the user before routing to specific urls
 * 
 * This class is used in app.router.ts for CanActivate property
 * 
 * This class should implement the CanActivate interface which forces us to implement
 * canActivate method, this method is called when specific URLs encountered
 *
 * @Injectable A marker metadata which specifies any class which can be 
 * injected can be injected to this class
 */
@Injectable()
export class RoutingGuard implements CanActivate {

  /** constructor will be executed on creation of object creation 
   * 
   * the objects specified as parameters will be injected while execution 
   * and these are used as instance variables 
   */
  constructor(private _route: Router) { }

  /** This method checks whether the user has logged in, by checking the session
  * 
  * if user logged in then it continues to route
  * else it is routed to error page
  */
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    let user: User = JSON.parse(sessionStorage.getItem("user"));

    if (user == null) {
      this._route.navigate(['error']);
      return false;
    } else {
      return true;
    }
  }
}
