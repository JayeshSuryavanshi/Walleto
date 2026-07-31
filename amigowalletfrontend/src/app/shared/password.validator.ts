import { AbstractControl, ValidationErrors } from '@angular/forms';

/**
 * Password policy validators. Each returns an error object when the rule fails,
 * or null when it passes. Also used imperatively (e.g. in login) to pre-check.
 */
export class PasswordValidator {
  static requiredAUpperCase(control: AbstractControl): ValidationErrors | null {
    const containUpperCase = /^.*[A-Z].*$/;
    if (control.value == null || control.value === '' || !containUpperCase.test(control.value)) {
      return { uppercase: true };
    }
    return null;
  }

  static requiredALowerCase(control: AbstractControl): ValidationErrors | null {
    const containLowerCase = /^.*[a-z].*$/;
    if (control.value == null || control.value === '' || !containLowerCase.test(control.value)) {
      return { lowercase: true };
    }
    return null;
  }

  static requiredANumber(control: AbstractControl): ValidationErrors | null {
    const containNumber = /^.*[0-9].*$/;
    if (control.value == null || control.value === '' || !containNumber.test(control.value)) {
      return { number: true };
    }
    return null;
  }

  static minLength(control: AbstractControl): ValidationErrors | null {
    if (control.value == null || control.value === '' || control.value.length < 8) {
      return { minlength: true };
    }
    return null;
  }

  static maxLength(control: AbstractControl): ValidationErrors | null {
    if (control.value == null || control.value === '' || control.value.length > 20) {
      return { maxlength: true };
    }
    return null;
  }

  static requiredASpecialChar(control: AbstractControl): ValidationErrors | null {
    const containSpecialChar = /^.*[!#$%^&*()].*$/;
    if (control.value == null || control.value === '' || !containSpecialChar.test(control.value)) {
      return { special: true };
    }
    return null;
  }
}
