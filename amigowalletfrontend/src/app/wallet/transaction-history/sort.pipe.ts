import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sort'
})
export class SortPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (args === "userTransactionId") {
      return value.sort((a: any, b: any) => {
        if (a.userTransactionId < b.userTransactionId) {
          return -1;
        } else if (a.userTransactionId > b.userTransactionId) {
          return 1;
        } else {
          return 0;
        }
      });
    }
    else if (args === "transactionDateTime") {
      return value.sort((a: any, b: any) => {
        a = new Date(a.transactionDateTime);
        b = new Date(b.transactionDateTime);
        if (a < b) {
          return -1;
        } else if (a > b) {
          return 1;
        } else {
          return 0;
        }
      });
    }
    else if (args === "userTransactionIdRev") {
      return value.sort((a: any, b: any) => {
        if (a.userTransactionId > b.userTransactionId) {
          return -1;
        } else if (a.userTransactionId < b.userTransactionId) {
          return 1;
        } else {
          return 0;
        }
      });
    }
    else if (args === "transactionDateTimeRev") {
      return value.sort((a: any, b: any) => {
        a = new Date(a.transactionDateTime);
        b = new Date(b.transactionDateTime);
        if (a > b) {
          return -1;
        } else if (a < b) {
          return 1;
        } else {
          return 0;
        }
      });
    }
    return value;
  }

}
