import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ErrorCampo } from '../../../components/error-campo/error-campo';
import { PasswordChecklist } from '../../../components/password-checklist/password-checklist';
import { UsuarioService } from '../../../services/usuario.service';
import {
  PASSWORD_MAX,
  passwordSeguraValidator,
  passwordsCoincidenValidator,
} from '../../../validators/password.validators';
import { noSoloEspaciosValidator } from '../../../validators/texto.validators';
import {
  PATRON_EMAIL,
  PATRON_NOMBRE_COMPLETO,
  PATRON_NOMBRE_USUARIO,
  emailDisponible,
  nombreUsuarioDisponible,
} from '../../../validators/usuario.validators';

// Pagina de registro de usuarios nuevos, siempre quedan con rol USUARIO
@Component({
  selector: 'app-registro',
  imports: [ReactiveFormsModule, RouterLink, ErrorCampo, PasswordChecklist],
  templateUrl: './registro.html',
})
export class Registro {
  private readonly fb = inject(FormBuilder);
  private readonly usuarios = inject(UsuarioService);
  private readonly router = inject(Router);

  protected readonly verPassword = signal(false);
  protected readonly passwordMax = PASSWORD_MAX;

  readonly form = this.fb.nonNullable.group(
    {
      nombreCompleto: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100), Validators.pattern(PATRON_NOMBRE_COMPLETO), noSoloEspaciosValidator]],
      nombreUsuario: ['', [Validators.required, Validators.pattern(PATRON_NOMBRE_USUARIO), nombreUsuarioDisponible(this.usuarios)]],
      email: ['', [Validators.required, Validators.maxLength(100), Validators.email, Validators.pattern(PATRON_EMAIL), emailDisponible(this.usuarios)]],
      password: ['', [Validators.required, passwordSeguraValidator]],
      confirmar: ['', Validators.required],
      aceptaNormas: [false, Validators.requiredTrue],
    },
    { validators: passwordsCoincidenValidator('password', 'confirmar') },
  );

  readonly mensajesNombre = { pattern: 'Solo se permiten letras y espacios.' };
  readonly mensajesUsuario = { pattern: 'Debe tener entre 4 y 20 caracteres: Letras, números o guion bajo (_).' };
  readonly mensajesEmail = { pattern: 'Ingresa un correo electrónico válido (ej. nombre@duocuc.cl).' };

  get noCoinciden(): boolean {
    return this.form.hasError('passwordsNoCoinciden') && this.form.controls.confirmar.touched;
  }

  registrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { nombreCompleto, nombreUsuario, email, password } = this.form.getRawValue();
    this.usuarios.registrar({ nombreCompleto, nombreUsuario, email, password });
    this.router.navigate(['/login'], { queryParams: { registrado: 1 } });
  }
}
