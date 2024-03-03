import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'humanize'
})
export class HumanizePipe implements PipeTransform {
    transform(value: any): string {
        if (isNaN(+value)) {
            return "NA"
        }
        if (!isFinite(+value)) {
            return "NA"
        }     
        return (+value).toLocaleString('en-IN', {maximumFractionDigits: 2})      
    }
}