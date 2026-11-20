import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { AbstractControl } from '@angular/forms';

const MENSAJES_BASE: Record<string, (error: any) => string> = {
  required: () => 'Este campo es obligatorio.',
  requiredTrue: () => 'Debes aceptar para continuar.',
  minlength: (e) => `Debe tener al menos ${e.requiredLength} caracteres (llevas ${e.actualLength}).`,
  maxlength: (e) => `No puede superar los ${e.requiredLength} caracteres.`,
  email: () => 'Ingresa un correo electrónico válido.',
  pattern: () => 'El formato ingresado no es válido.',
  soloEspacios: () => 'No puede contener solo espacios.',
  nombreUsuarioTomado: () => 'Ese nombre de usuario ya está registrado.',
  emailTomado: () => 'Ese correo electrónico ya está registrado.',
  passwordInsegura: () => 'La contraseña no cumple todos los requisitos de seguridad.',
};

// Muestra el mensaje de error de un campo del formulario cuando ya fue tocado
@Component({
  selector: 'app-error-campo',
  templateUrl: './error-campo.html',
  // Eager para que se actualice cada vez que cambia el formulario del padre
  changeDetection: ChangeDetectionStrategy.Eager,
})
export class ErrorCampo {
  readonly control = input.required<AbstractControl>();
  readonly mensajes = input<Record<string, string>>({});
  // Error que viene del grupo, por ejemplo cuando las contrasenas no coinciden
  readonly errorExtra = input<string | null>(null);

  mensaje(): string | null {
    const control = this.control();
    if (!control.touched && !control.dirty) return null;
    if (control.errors) {
      const clave = Object.keys(control.errors)[0];
      const personalizado = this.mensajes()[clave];
      if (personalizado) return personalizado;
      const base = MENSAJES_BASE[clave];
      return base ? base(control.errors[clave]) : 'El valor ingresado no es válido.';
    }
    return control.touched ? this.errorExtra() : null;
  }
}
