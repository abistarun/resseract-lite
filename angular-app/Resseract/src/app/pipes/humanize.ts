import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'humanize'
})
export class HumanizePipe implements PipeTransform {
    transform(value: any): string {
        const isFloat = (n) => {
            return n === +n && n !== (n | 0);
        }
        return isFloat(+value) ? (+value).toFixed(2) : value;
    }
}