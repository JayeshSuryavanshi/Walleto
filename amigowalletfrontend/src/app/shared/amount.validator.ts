import { AbstractControl, ValidationErrors } from '@angular/forms';

/** Custom validator: amount must be strictly greater than 0. */
export class AmountValidator {
  static min(control: AbstractControl): ValidationErrors | null {
    if (control.value <= 0) {
      return { min: true };
    }
    return null;
  }
}
