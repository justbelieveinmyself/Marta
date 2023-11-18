import {Component, OnInit} from '@angular/core';
import {LocalUser} from "../models/local-user";
import {UserService} from "../service/user.service";

@Component({
    selector: 'app-admin-page',
    templateUrl: './admin-page.component.html',
    styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
    constructor(
        private userService: UserService
    ) {}
    users: LocalUser[];
    indexItemInModal: number = null;
    ngOnInit(): void {
        this.userService.getUsers().subscribe({
            next: users => {
                this.users = users;
            },
            error: err => console.log(err)
        });
    }

    deleteUser(user: LocalUser) {
        this.userService.deleteUser(user.id).subscribe({
            next: result => {
                this.users.splice(this.users.indexOf(user), 1)
            },
            error: err => console.log(err)
        })
    }

    changeRole(role: string) {
        const user = this.users.at(this.indexItemInModal);
        if (user.roles.includes(role)) {
            user.roles.splice(user.roles.indexOf(role), 1)
        } else {
            user.roles.push(role)
        }
    }
}
