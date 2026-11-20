import { DatePipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ErrorCampo } from '../../components/error-campo/error-campo';
import { PasswordChecklist } from '../../components/password-checklist/password-checklist';
import { RolBadge } from '../../components/rol-badge/rol-badge';
import { AuthService } from '../../services/auth.service';
import { ForoService } from '../../services/foro.service';
import { UsuarioService } from '../../services/usuario.service';
import {
  PASSWORD_MAX,
  passwordDistintaValidator,
  passwordSeguraValidator,
  passwordsCoincidenValidator,
} from '../../validators/password.validators';
import { noSoloEspaciosValidator } from '../../validators/texto.validators';
import { PATRON_EMAIL, PATRON_NOMBRE_COMPLETO, emailDisponible } from '../../validators/usuario.validators';

// Modificacion de perfil: Datos personales y cambio de contrasena
@Component({
  selector: 'app-perfil',
  imports: [ReactiveFormsModule, DatePipe, ErrorCampo, PasswordChecklist, RolBadge],
  templateUrl: './perfil.html',
})
export class Perfil implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly usuarios = inject(UsuarioService);
  private readonly foro = inject(ForoService);
  protected readonly auth = inject(AuthService);

  protected readonly mensajeDatos = signal<string | null>(null);
  protected readonly mensajePassword = signal<string | null>(null);
  protected readonly passwordMax = PASSWORD_MAX;

  readonly totalTemas = computed(() => {
    const usuario = this.auth.usuarioActual();
    return usuario ? this.foro.temasDeUsuario(usuario.idUsuario).length : 0;
  });
  readonly totalComentarios = computed(() => {
    const usuario = this.auth.usuarioActual();
    return usuario ? this.foro.comentarios().filter((c) => c.idUsuario === usuario.idUsuario).length : 0;
  });

  private readonly idActual = () => this.auth.usuarioActual()?.idUsuario;

  readonly formDatos = this.fb.nonNullable.group({
    nombreCompleto: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100), Validators.pattern(PATRON_NOMBRE_COMPLETO), noSoloEspaciosValidator]],
    email: ['', [Validators.required, Validators.maxLength(100), Validators.email, Validators.pattern(PATRON_EMAIL), emailDisponible(this.usuarios, this.idActual)]],
  });

  readonly formPassword = this.fb.nonNullable.group(
    {
      actual: ['', [Validators.required, (control: AbstractControl) => this.validarPasswordActual(control)]],
      nueva: ['', [Validators.required, passwordSeguraValidator]],
      confirmar: ['', Validators.required],
    },
    { validators: [passwordsCoincidenValidator('nueva', 'confirmar'), passwordDistintaValidator('actual', 'nueva')] },
  );

  readonly mensajesNombre = { pattern: 'Solo se permiten letras y espacios.' };
  readonly mensajesEmail = { pattern: 'Ingresa un correo electrónico válido (ej. nombre@duocuc.cl).' };

  ngOnInit(): void {
    this.cargarDatos();
  }

  get noCoinciden(): boolean {
    return this.formPassword.hasError('passwordsNoCoinciden') && this.formPassword.controls.confirmar.touched;
  }

  get repetida(): boolean {
    return this.formPassword.hasError('passwordRepetida') && this.formPassword.controls.nueva.touched;
  }

  guardarDatos(): void {
    const usuario = this.auth.usuarioActual();
    if (!usuario || this.formDatos.invalid) {
      this.formDatos.markAllAsTouched();
      return;
    }
    const { nombreCompleto, email } = this.formDatos.getRawValue();
    this.usuarios.actualizarPerfil(usuario.idUsuario, nombreCompleto, email);
    this.cargarDatos();
    this.mensajeDatos.set('Perfil actualizado: Tus datos se guardaron correctamente.');
  }

  cambiarPassword(): void {
    const usuario = this.auth.usuarioActual();
    if (!usuario || this.formPassword.invalid) {
      this.formPassword.markAllAsTouched();
      return;
    }
    this.usuarios.cambiarPassword(usuario.idUsuario, this.formPassword.getRawValue().nueva);
    this.formPassword.reset();
    this.mensajePassword.set('Contraseña actualizada: Úsala la próxima vez que inicies sesión.');
  }

  cargarDatos(): void {
    const usuario = this.auth.usuarioActual();
    if (usuario) {
      this.formDatos.reset({ nombreCompleto: usuario.nombreCompleto, email: usuario.email });
    }
  }

  private validarPasswordActual(control: AbstractControl): ValidationErrors | null {
    const valor = control.value as string;
    if (!valor) return null;
    return valor === this.auth.usuarioActual()?.password ? null : { passwordIncorrecta: true };
  }
}
