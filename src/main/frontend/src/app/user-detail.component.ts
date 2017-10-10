import { Component, Input } from '@angular/core';

import { User } from './user';

@Component({
  selector: 'app-user-detail',
  template: `
    <div *ngIf="user" class="card">
      <h4 class="card-header">
        <span class="font-weight-bold">User #{{user.id}}</span>
      </h4>
      <div class="card-body">
        <div class="form-group">
          <label for="username">User Name</label>
          <input id="username" class="form-control" [(ngModel)]="user.name" placeholder="name" readonly/>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input id="email" class="form-control" [(ngModel)]="user.email" placeholder="email" readonly/>
        </div>
      </div>
    </div>
  `
})
export class UserDetailComponent {
  @Input() user: User;
}
