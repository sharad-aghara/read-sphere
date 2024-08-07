import { CommonModule, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationRequest } from '../../services/models/authentication-request';
import { AuthenticationService } from '../../services/services/authentication.service';
import { TokenService } from '../../services/token/token.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule, NgIf],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  // providers: [provideHttpClient()]
})
export class LoginComponent {

  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ) { }

  login() {
    // every time clear the old error message and show new one
    this.errorMsg = [];
    this.authService.authenticate(
      {
        body: this.authRequest
      }
    ).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        // console.log("token: ", localStorage.getItem('token'));
        this.router.navigate(['books']);
      },

      error: (err) => {
        console.log(err);
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          console.log(err.error)
          this.errorMsg.push(err.error.errorMsg ? err.error.errorMsg : err.error.errorDescription);
        }
      }

    })
  }

  activateAccount() {
    this.router.navigate(['activate-account']);
  }

  register() {
    this.router.navigate(['register']);
  }
}
