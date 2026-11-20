import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { UsuarioService } from '../services/usuario.service';

export const PATRON_NOMBRE_USUARIO = /^[A-Za-z0-9_]{4,20}$/;
export const PATRON_NOMBRE_COMPLETO = /^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ' ]+$/;
// Un poco mas estricto que Validators.email, exige dominio con punto
export const PATRON_EMAIL = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

// Revisa en el repositorio en memoria que el nombre de usuario no exista
export function nombreUsuarioDisponible(usuarios: UsuarioService, excluirId?: () => number | undefined): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = (control.value ?? '') as string;
    if (!valor) return null;
    return usuarios.existeNombreUsuario(valor, excluirId?.()) ? { nombreUsuarioTomado: true } : null;
  };
}

export function emailDisponible(usuarios: UsuarioService, excluirId?: () => number | undefined): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = (control.value ?? '') as string;
    if (!valor) return null;
    return usuarios.existeEmail(valor, excluirId?.()) ? { emailTomado: true } : null;
  };
}
