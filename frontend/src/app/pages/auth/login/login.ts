import { Component, inject, input, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ErrorCampo } from '../../../components/error-campo/error-campo';
import { AuthService } from '../../../services/auth.service';
import { noSoloEspaciosValidator } from '../../../validators/texto.validators';

// Pagina de inicio de sesion
@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink, ErrorCampo],
  templateUrl: './login.html',
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  // Query params que llegan desde otras paginas
  readonly volver = input<string>();
  readonly registrado = input<string>();
  readonly recuperado = input<string>();

  protected readonly error = signal<string | null>(null);
  protected readonly verPassword = signal(false);

  readonly cuentasDemo = [
    { usuario: 'admin', password: 'Admin2026', rol: 'Administrador' },
    { usuario: 'mod_camila', password: 'Moder2026', rol: 'Moderador' },
    { usuario: 'juanito_dev', password: 'Usuario2026', rol: 'Usuario' },
  ];

  readonly form = this.fb.nonNullable.group({
    credencial: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(100), noSoloEspaciosValidator]],
    password: ['', [Validators.required, Validators.maxLength(20)]],
  });

  usarCuenta(usuario: string, password: string): void {
    this.form.setValue({ credencial: usuario, password });
    this.error.set(null);
  }

  ingresar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { credencial, password } = this.form.getRawValue();
    const resultado = this.auth.login(credencial, password);
    if (!resultado.ok) {
      this.error.set(resultado.mensaje);
      return;
    }
    const destino = this.volver();
    this.router.navigateByUrl(destino && destino.startsWith('/') ? destino : '/inicio');
  }
}
