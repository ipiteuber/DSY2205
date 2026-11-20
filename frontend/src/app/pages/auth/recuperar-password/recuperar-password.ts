import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ErrorCampo } from '../../../components/error-campo/error-campo';
import { PasswordChecklist } from '../../../components/password-checklist/password-checklist';
import { AuthService } from '../../../services/auth.service';
import {
  PASSWORD_MAX,
  passwordSeguraValidator,
  passwordsCoincidenValidator,
} from '../../../validators/password.validators';
import { PATRON_EMAIL } from '../../../validators/usuario.validators';

// Recuperar contrasena simulado: Se pide el correo, se muestra el codigo y se define una nueva
@Component({
  selector: 'app-recuperar-password',
  imports: [ReactiveFormsModule, RouterLink, ErrorCampo, PasswordChecklist],
  templateUrl: './recuperar-password.html',
})
export class RecuperarPassword {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly paso = signal<1 | 2>(1);
  protected readonly codigoEnviado = signal<string | null>(null);
  protected readonly correo = signal('');
  protected readonly error = signal<string | null>(null);
  protected readonly passwordMax = PASSWORD_MAX;

  readonly formCorreo = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.maxLength(100), Validators.email, Validators.pattern(PATRON_EMAIL)]],
  });

  readonly formPassword = this.fb.nonNullable.group(
    {
      codigo: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
      password: ['', [Validators.required, passwordSeguraValidator]],
      confirmar: ['', Validators.required],
    },
    { validators: passwordsCoincidenValidator('password', 'confirmar') },
  );

  get noCoinciden(): boolean {
    return this.formPassword.hasError('passwordsNoCoinciden') && this.formPassword.controls.confirmar.touched;
  }

  enviarCodigo(): void {
    if (this.formCorreo.invalid) {
      this.formCorreo.markAllAsTouched();
      return;
    }
    const email = this.formCorreo.getRawValue().email;
    const resultado = this.auth.solicitarCodigo(email);
    if (!resultado.ok || !resultado.codigo) {
      this.error.set(resultado.mensaje);
      return;
    }
    this.error.set(null);
    this.correo.set(email);
    this.codigoEnviado.set(resultado.codigo);
    this.formPassword.reset();
    this.paso.set(2);
  }

  cambiarPassword(): void {
    if (this.formPassword.invalid) {
      this.formPassword.markAllAsTouched();
      return;
    }
    const { codigo, password } = this.formPassword.getRawValue();
    const resultado = this.auth.restablecerPassword(this.correo(), codigo, password);
    if (!resultado.ok) {
      this.error.set(resultado.mensaje);
      return;
    }
    this.router.navigate(['/login'], { queryParams: { recuperado: 1 } });
  }

  volverAlCorreo(): void {
    this.error.set(null);
    this.codigoEnviado.set(null);
    this.paso.set(1);
  }
}
