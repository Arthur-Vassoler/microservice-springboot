import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterService } from 'src/app/core/services/register.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private registerService: RegisterService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  save() {
    if (this.form.valid) {
      this.registerService.register(this.form.value).subscribe({
        next: (val) => {
          this.openSuccessSnackBar('Cadastro realizado com sucesso');
          this.router.navigateByUrl('/signin')
        }
      })
    } else {
      alert('Por favor, preencha o formulário corretamente.');
    }
  }

  openSuccessSnackBar(message: string) {
    this.snackBar.open(message, 'Fechar', {
      duration: 10000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
  }
}
