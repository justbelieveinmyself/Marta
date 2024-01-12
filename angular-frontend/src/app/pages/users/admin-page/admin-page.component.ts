import {Component, OnInit} from '@angular/core';
import {LocalUser} from "../../../models/local-user";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-admin-page',
    templateUrl: './admin-page.component.html',
    styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {
    constructor(
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}
    users: LocalUser[];
    indexItemInModal: number = null;
    newRoles : string[] = [];
    ngOnInit(): void {
        this.activatedRoute.data.subscribe(data => {
            this.users = data["users"];
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

    changeStateRole(role: string) {
        if (this.newRoles.includes(role)) {
            this.newRoles.splice(this.newRoles.indexOf(role), 1)
        } else {
            this.newRoles.push(role);
        }
    }

    saveRolesChanges() {
        let localUser = this.users.at(this.indexItemInModal);
        this.userService.updateRolesById(localUser.id, this.newRoles).subscribe({
            next: success => {
                this.users.at(this.indexItemInModal).roles = this.newRoles;
                console.log(localUser)
            },
            error: err => {
                this.newRoles = localUser.roles;
            }
        } );
    }
}
