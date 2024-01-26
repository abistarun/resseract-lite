import { Component, OnInit, Input, IterableDiffers } from '@angular/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';

@Component({
  selector: 'app-selector-wrapper',
  templateUrl: './selector-wrapper.component.html',
  styleUrls: ['./selector-wrapper.component.css']
})
export class SelectorWrapperComponent implements OnInit {

  @Input() keys: string[];
  keysDiffer: any;
  @Input() placeholder: string;
  @Input() allowMultiple: boolean = false;
  @Input() onSelectCallback: Function;
  @Input() defaultValue: string;

  filteredKeys: string[];
  keysBuffer: string[] = [];
  keysBufferSize = 50
  keysLoading: boolean = false;
  keyInput$ = new Subject<string>();
  selectAll: boolean;
  selectedKey;

  constructor(differs: IterableDiffers) {
    this.keysDiffer = differs.find([]).create(null);
  }

  ngOnInit() {
    this.searchKeys();
    this.loadKeys();
  }

  ngDoCheck() {
    const change = this.keysDiffer.diff(this.keys);
    if (change)
      this.loadKeys();
  }

  getSelectedKey() {
    return this.selectedKey;
  }

  isSomethingSelected() {
    if (this.allowMultiple && this.selectedKey)
      return this.selectedKey.length != 0;
    return this.selectedKey;
  }

  onSelectAll(e) {
    if (e.target.checked) {
      this.selectedKey = this.filteredKeys;
    } else {
      this.selectedKey = null;
    }
    this.callOnSelectCallback();
  }

  onKeyScrollToEnd() {
    this.fetchMore();
  }

  searchKeys() {
    this.keyInput$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map(term => this.filterKeys(term))
    ).subscribe(ele => {
      this.filteredKeys = ele;
      this.keysBuffer = this.filteredKeys.slice(0, this.keysBufferSize);
    })
  }

  filterKeys(term) {
    if (term == null)
      return this.keys
    return this.keys.filter(dk => dk.toLowerCase().includes(term.toLowerCase()))
  }

  loadKeys(): void {
    this.filteredKeys = this.keys;
    this.keysBuffer = this.filteredKeys.slice(0, this.keysBufferSize);
    this.selectedKey = null;
    if (this.defaultValue)
      this.selectedKey = this.defaultValue;
  }

  onKeyScroll({ end }) {
    if (this.keysLoading || this.filteredKeys.length === this.keysBuffer.length) {
      return;
    }

    if (end + 10 >= this.keysBuffer.length) {
      this.fetchMore();
    }
  }

  onClear() {
    this.loadKeys();
  }

  private fetchMore() {
    const len = this.keysBuffer.length;
    const more = this.filteredKeys.slice(len, this.keysBufferSize + len);
    this.keysLoading = true;
    // using timeout here to simulate backend API delay
    setTimeout(() => {
      this.keysLoading = false;
      this.keysBuffer = this.keysBuffer.concat(more);
    }, 200)
  }

  callOnSelectCallback() {
    if (this.onSelectCallback)
      this.onSelectCallback(this.getSelectedKey());
  }
}
