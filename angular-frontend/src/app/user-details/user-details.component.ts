import { Component, OnInit } from '@angular/core';
import { TokenService } from '../service/token.service';
import { LocalUser } from '../models/local-user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../service/user.service';
import { ImageService } from '../service/image.service';
@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['../user-profile/user-profile.component.css']
})
export class UserDetailsComponent implements OnInit {
  user!: LocalUser;

  newemail!: string; //useless
  emailForm!: FormGroup;
  nameForm!: FormGroup;
  submittedEmailForm = false;
  submittedNameForm = false;
  constructor(
    private tokenService: TokenService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private imageService: ImageService
  ){}
  ngOnInit(){
    this.user = this.tokenService.getUser();
    this.emailForm = this.formBuilder.group({
      email: [this.user.email, [Validators.required, Validators.email]],
    });
    this.nameForm = this.formBuilder.group({
      name: [this.user.firstName, [Validators.required, Validators.nullValidator] ],
      surname: [this.user.lastName, [Validators.required, Validators.nullValidator] ]
    })
    if(this.user != null){
      this.imageService.getUserAvatar(this.user.id).then(res => this.user.avatar = res).
        catch(error => this.user.avatar = "https://eliaslealblog.files.wordpress.com/2014/03/user-200.png");
    }
  }
  get fEmail() { return this.emailForm.controls; }
  get fName(){
    return this.nameForm.controls;
  }
  changeEmail(){
    this.submittedEmailForm = true;
    if(this.emailForm.invalid){
      return;
    }
    this.userService.updateEmail(this.user.id, this.emailForm.value.email);
    this.user.email = this.emailForm.value.email;
    // var modalEmail = document.getElementById('modalEmail');
    // jQuery('#modalEmail').toggle();

  }
  onFileSelected(event : any){
    this.userService.updateAvatar(
      this.user.id,
      event.target.files[0]).subscribe(data => {
        localStorage.removeItem("avatar");
        this.imageService.getUserAvatar(this.user.id).then(data => this.user.avatar = data);
        console.log(data);
      }
    );
  }
  onGenderChanged(event : any){
    var gender;
    if(event.target.id == "inlineRadio1"){
      gender = "Male";
    }else{
      gender = "Female";
    }
    console.log(gender);
    this.userService.updateGender(this.user.id, gender);
  }
  changeName(){
    this.submittedNameForm = true;
    if(this.nameForm.invalid){
      return;
    }
    this.userService.updateNames(this.user.id, this.nameForm.value.name, this.nameForm.value.surname);
    this.user.firstName = this.nameForm.value.name;
    this.user.lastName = this.nameForm.value.surname;

  }

}
