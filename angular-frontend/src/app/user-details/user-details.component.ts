import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { TokenService } from '../service/token.service';
import { LocalUser } from '../models/local-user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../service/user.service';
@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['../user-profile/user-profile.component.css']
})
export class UserDetailsComponent implements OnInit {
  user!: LocalUser;
  
  newemail!: string; //useless
  emailForm!: FormGroup;
  submitted = false;
  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private formBuilder: FormBuilder
  ){}
  ngOnInit(){
    this.user = this.tokenService.getUser();
    this.emailForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }
  get f() { return this.emailForm.controls; }
  changeEmail(){
    this.submitted = true;
    if(this.emailForm.invalid){
      return;
    }
    // this.userService.updateEmail(this.user.id, this.emailForm.value.email);
    // var modalEmail = document.getElementById('modalEmail');
    // jQuery('#modalEmail').toggle();
    
  }
}