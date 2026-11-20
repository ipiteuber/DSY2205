import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// Evita que un campo pase el required solo con espacios en blanco
export const noSoloEspaciosValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const valor = control.value;
  if (typeof valor !== 'string' || valor.length === 0) return null;
  return valor.trim().length === 0 ? { soloEspacios: true } : null;
};
