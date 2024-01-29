import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class PageDataService {

    private _pageNumber: number;
    private _sizeOfPage: number;
    private _isSortASC: boolean;
    private _isFilteredByWithPhoto: boolean;
    private _isFilteredByVerified: boolean;
    private _sortBy: string;
    private _searchWord: string;

    get pageNumber(): number {
        return this._pageNumber;
    }

    set pageNumber(value: number) {
        this._pageNumber = value;
    }

    get sizeOfPage(): number {
        return this._sizeOfPage;
    }

    set sizeOfPage(value: number) {
        this._sizeOfPage = value;
    }

    get isSortASC(): boolean {
        return this._isSortASC;
    }

    set isSortASC(value: boolean) {
        this._isSortASC = value;
    }

    get isFilteredByWithPhoto(): boolean {
        return this._isFilteredByWithPhoto;
    }

    set isFilteredByWithPhoto(value: boolean) {
        this._isFilteredByWithPhoto = value;
    }

    get isFilteredByVerified(): boolean {
        return this._isFilteredByVerified;
    }

    set isFilteredByVerified(value: boolean) {
        this._isFilteredByVerified = value;
    }

    get sortBy(): string {
        return this._sortBy;
    }

    set sortBy(value: string) {
        this._sortBy = value;
    }

    get searchWord(): string {
        return this._searchWord;
    }

    set searchWord(value: string) {
        this._searchWord = value;
    }

    resetFilters() {
        this._isFilteredByVerified = false;
        this._isFilteredByWithPhoto = false;
    }
}
