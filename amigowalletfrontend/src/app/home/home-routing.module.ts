import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./home.component";
import { ForgotPasswordComponent } from "./forgot-password/forgot-password.component";
import { SecurityQuestionComponent } from './security-question/security-question.component';

const routes: Routes = [
   
    /** If any application encounters login in url it will load WelcomeComponent  
     * which will internally load login and register component
    */
    { path: 'login', component: HomeComponent },

    /** If any application encounters otp in url it will load SecurityQuestionComponent
     */
    { path: 'security', component: SecurityQuestionComponent },

    /** If any application encounters forgotPassword in url it will load ForgotPasswordComponent  
     * which is the option given for the visiter i.e. user who forgot his password
     */
    { path: 'forgotPassword', component: ForgotPasswordComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
