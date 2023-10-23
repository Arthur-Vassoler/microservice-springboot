import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from '../types/types';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  private apiUrl = environment.apiUrl

  constructor(private http: HttpClient) { }

  register(User: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/api/auth/signup`, User)
  }

  update(userId: string, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/api/users/id/${userId}`, user);
  }
}
