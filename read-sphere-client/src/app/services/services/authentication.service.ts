/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiConfiguration } from '../api-configuration';
import { BaseService } from '../base-service';
import { StrictHttpResponse } from '../strict-http-response';

import { Authenticate$Params } from '../fn/authentication/authenticate';
import { confirm, Confirm$Params } from '../fn/authentication/confirm';
import { register, Register$Params } from '../fn/authentication/register';
import { AuthenticationResponse } from '../models/authentication-response';

@Injectable({ providedIn: 'root' })
export class AuthenticationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `register()` */
  static readonly RegisterPath = '/auth/register';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `register()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  // register$Response(params: Register$Params, context?: HttpContext): Observable<StrictHttpResponse<{
  // }>> {
  //   return register(this.http, this.rootUrl, params, context);
  // }

  register$Response(params: Register$Params, context?: HttpContext): Observable<HttpResponse<string>> {
    return this.http.post<string>(
      `${this.rootUrl}`, // Update this with the correct endpoint if needed
      params.body,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response',
        context
      }
    );
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `register$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  // register(params: Register$Params, context?: HttpContext): Observable<{
  // }> {
  //   return this.register$Response(params, context).pipe(
  //     map((r: StrictHttpResponse<{
  //     }>): {
  //       } => r.body)
  //   );
  // }

  register(params: Register$Params, context?: HttpContext): Observable<string> {
    return this.register$Response(params, context).pipe(
      map((r: HttpResponse<string>) => r.body as string)
    );
  }
  

  /** Path part for operation `authenticate()` */
  static readonly AuthenticatePath = '/auth/authenticate';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `authenticate()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  // authenticate$Response(params: Authenticate$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticationResponse>> {
  //   return authenticate(this.http, this.rootUrl, params, context);
  // }

  authenticate$Response(params: Authenticate$Params, context?: HttpContext): Observable<HttpResponse<AuthenticationResponse>> {
    return this.http.post<AuthenticationResponse>(
      `${this.rootUrl}${AuthenticationService.AuthenticatePath}`,
      params.body,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        observe: 'response',
        context
      }
    );
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `authenticate$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  // authenticate(params: Authenticate$Params, context?: HttpContext): Observable<AuthenticationResponse> {
  //   return this.authenticate$Response(params, context).pipe(
  //     map((r: StrictHttpResponse<AuthenticationResponse>): AuthenticationResponse => r.body)
  //   );

  authenticate(params: Authenticate$Params, context?: HttpContext): Observable<AuthenticationResponse> {
    return this.authenticate$Response(params, context).pipe(
      map((r: HttpResponse<AuthenticationResponse>) => r.body as AuthenticationResponse)
    );

  }

  /** Path part for operation `confirm()` */
  static readonly ConfirmPath = '/auth/activate-account';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `confirm()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirm$Response(params: Confirm$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return confirm(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `confirm$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  confirm(params: Confirm$Params, context?: HttpContext): Observable<void> {
    return this.confirm$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
