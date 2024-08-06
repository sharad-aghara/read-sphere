import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { importProvidersFrom } from '@angular/core';
import { HttpTokenInterceptor } from './app/services/interceptor/http-token.interceptor';
import { TokenService } from './app/services/token/token.service';

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(
      provideHttpClient(
        withInterceptors([HttpTokenInterceptor])
      )
    ),
    TokenService
  ]
}).catch(err => console.error(err));
