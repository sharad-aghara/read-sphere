import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from '../token/token.service';

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

  constructor(
    private tokenService: TokenService
  ){}
  
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {    
    // const token = this.tokenService.token;
    const token = localStorage.getItem('token');
    console.log("token-interseptor: ", token);

    if(token) {
      const authReq = request
      .clone({
        headers: new HttpHeaders({
          Authorization: 'Bearer ' + token
        })
      })

      console.log('HTTP Request with Authorization Header:', authReq);

      return next.handle(authReq);
    }

    console.log('HTTP Request without Authorization Header:', request);
    
    return next.handle(request);
  }
  
}