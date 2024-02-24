import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'humanize'
})
export class HumanizePipe implements PipeTransform {
    transform(value: any): string {
        const isFloat = (n) => {
            return n === +n && n !== (n | 0);
        }
        if (isNaN(+value)) {
            return "NA"
        }
        if (isFloat(+value)) {
            if (!isFinite(+value)) {
                return "NA"
            }     
            return (+value).toLocaleString('en-IN', {maximumFractionDigits: 2})      
        }
        return value;
    }
}