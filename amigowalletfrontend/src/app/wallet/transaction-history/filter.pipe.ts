import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (args=="C") {
      return value.filter((a:any) => {
        return a.paymentType.paymentType == "C";
      });
    }
    else if (args=="D") {
      return value.filter((a:any) => {
        return a.paymentType.paymentType == "D";
      });
    }
    else {
      return value;
    }
  }

}
