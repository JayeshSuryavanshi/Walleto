import { AbstractControl } from '@angular/forms';

/** custom validator method for checking the amount is greater than 0 */

export class PasswordValidator {

    static requiredAUpperCase(control: AbstractControl): any {
        let containUpperCase: RegExp = /^.*[A-Z].*$/;
        if ((control.value == '' || control.value == null) || !(containUpperCase.test(control.value))) {
            return { "uppercase": true };
        }
        return null;
    }
    static requiredALowerCase(control: AbstractControl): any {
        let containLowerCase: RegExp = /^.*[a-z].*$/;
        if ((control.value == '' || control.value == null) || !(containLowerCase.test(control.value))) {
            return { "lowercase": true };
        }
        return null;
    }
    static requiredANumber(control: AbstractControl): any {
        let containNumber: RegExp = /^.*[0-9].*$/;
        if ((control.value == '' || control.value == null) || !(containNumber.test(control.value)) ) {
            return { "number": true };
        }
        return null;
    }
    static minLength(control: AbstractControl): any {
        if ((control.value == '' || control.value == null) || control.value.length < 8 ) {
            return { "minlength": true };
        }
        return null;
    }
    static maxLength(control: AbstractControl): any {
        if ((control.value == '' || control.value == null) || control.value.length > 20 ) {
            return { "maxlength": true };
        }
        return null;
    }
    static requiredASpecialChar(control: AbstractControl): any {
        let containSpecialChar: RegExp = /^.*[!#$%^&*()].*$/;
        if ((control.value == '' || control.value == null) || !(containSpecialChar.test(control.value))) {
            return { "special": true };
        }
        return null;
    }

}