import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Injectable()
export class AuthGuard {
  constructor(private userService: UserService, private router: Router) {}

  canActivate(): boolean {
    if (this.userService.statusLogged()) {
      return true;
    } else {
      this.router.navigate(['signup']);
      return false;
    }
  }
}
