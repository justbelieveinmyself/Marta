
export class Carousel {
    constructor(urls: string[]) {
        this.items = urls;
    }

    items: string[];
    activeId: number = 0;
    translatePosY: number = 0;

    setActive(id: number) {
        this.activeId = id;
    }

    getActive(): string {
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

}
