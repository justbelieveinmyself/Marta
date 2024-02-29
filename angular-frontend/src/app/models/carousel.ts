
export class Carousel<T> {
    constructor(items: T[]) {
        this.items = items;
    }

    items: T[];
    activeId: number = 0;
    translatePosY: number = 0;

    setActive(id: number) {
        this.activeId = id;
    }

    getActive(): T {
        return this.items[this.activeId];
    }

    previous() {
        if (this.activeId > 0) {
            this.activeId --;
        }
    }

    next() {
        if (this.items.length > this.activeId + 1) {
            this.activeId ++;
        }
    }

    isLast(): boolean {
        return this.items.length === this.activeId + 1;
    }

    isFirst(): boolean {
        return this.activeId === 0;
    }

}
