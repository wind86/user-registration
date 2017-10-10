import {Injectable} from '@angular/core';

import {Headers, Http, RequestOptions} from '@angular/http';

import 'rxjs/add/operator/toPromise';

import {User} from './user';
import {ValidationMessage} from './validation-message';
import {ValidationResponse} from './validation-response';
import {ServerError} from './server-error';
import {USERS} from './mock-users';

@Injectable()
export class UserService {

  private userUrl = document.getElementsByTagName('base')[0].href + 'api/user/';
  private headers = new Headers({ 'Content-Type': 'application/json' });

  constructor(private http: Http) {}

  getUsers(): Promise<User[]> {
    return this.http.get(this.userUrl)
      .toPromise()
      .then(response => response.json() as User[])
      .catch(this.handleError);
  }

  saveUser(user: User): Promise<User> {
    return this.http.post(this.userUrl,
        JSON.stringify(user),
        new RequestOptions({ headers: this.headers }))
      .toPromise()
      .then(response => response.json() as User)
      .catch(this.handleSaveError);
  }

  private handleSaveError(errorResponse: Response): Promise<any> {
    if (errorResponse.status === 400) {
      let validationResponse: ValidationResponse;
      validationResponse = new ValidationResponse();

      Object.assign(validationResponse, errorResponse.json());

      let messages: string[];
      messages = [];

      let validationErrors: ValidationMessage[];
      validationErrors = validationResponse.fieldErrors;

      let validation: ValidationMessage;
      for (validation of validationErrors) {
        messages.push(validation.message);
      }

      return Promise.reject(messages);
    }

    if (errorResponse.status === 500) {
      let serverError: ServerError;
      serverError = new ServerError();

      Object.assign(serverError, errorResponse.json());

      return Promise.reject([serverError.message]);
    }

    return this.handleError(errorResponse);
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
