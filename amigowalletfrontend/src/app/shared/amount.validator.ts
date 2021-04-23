import { AbstractControl } from '@angular/forms';

/** custom validator method for checking the amount is greater than 0 */

export class AmountValidator {

    /** if amount is less than or equal to 0 return { key: value } pair which specifies { errorMessage : true }
     *  if amount is valid sends null
     */
    static min(control: AbstractControl): any {
        if (control.value <= 0) {
            return { "min": true };
        }
        return null;
    }

}