import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserService } from './user.service';

interface LoginInputs {
  email: string,
  password: string
}

interface AuthResponse {
  token: string
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private apiUrl = environment.apiUrl

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) { }

  authenticate(login: LoginInputs): Observable<HttpResponse<AuthResponse>> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/api/auth/signin`,
      login,
      { observe: 'response' }
    ).pipe(
      tap((response) => {
        const authToken = response.body?.token || '';

        this.userService.saveToken(authToken);
      })
    );
  }
}