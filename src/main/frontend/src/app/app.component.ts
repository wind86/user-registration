import { Component, OnInit } from '@angular/core';

import { User } from './user';
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [UserService]
})

export class AppComponent implements OnInit {
  user = new User();

  users: User[];

  selectedUser: User;
  createdUser: User;

  errorMessage: any;
  successMessage: string;

  constructor(private userService: UserService) { }

  getUsers(): void {
    this.userService.getUsers().then(users => this.users = users, error =>  this.errorMessage = <any>error);
  }

  ngOnInit(): void {
    this.getUsers();
  }

  onSelect(user: User): boolean {
    this.selectedUser = user;
    return false;
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    this.userService.saveUser(this.user)
      .then(user => {
        this.createdUser = user;
        this.reset();
        this.users.push(user);
        this.successMessage = 'User registration is finished!';
      },
      error => this.errorMessage = <any>error
    );
  }

  private reset(): void {
    this.user.id = null;
    this.user.name = null;
    this.user.email = null;
    this.user.password = null;
  }
}
