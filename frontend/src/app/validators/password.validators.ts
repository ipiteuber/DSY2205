import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export interface ReglaPassword {
  clave: string;
  texto: string;
  cumple: (valor: string) => boolean;
}

export const PASSWORD_MIN = 8;
export const PASSWORD_MAX = 20;

// Reglas de seguridad de la contrasena, se usan en el validador y en el checklist
export const REGLAS_PASSWORD: ReglaPassword[] = [
  { clave: 'largoMinimo', texto: `Mínimo ${PASSWORD_MIN} caracteres`, cumple: (v) => v.length >= PASSWORD_MIN },
  { clave: 'largoMaximo', texto: `Máximo ${PASSWORD_MAX} caracteres`, cumple: (v) => v.length > 0 && v.length <= PASSWORD_MAX },
  { clave: 'mayuscula', texto: 'Al menos una letra mayúscula', cumple: (v) => /[A-ZÑ]/.test(v) },
  { clave: 'minuscula', texto: 'Al menos una letra minúscula', cumple: (v) => /[a-zñ]/.test(v) },
  { clave: 'numero', texto: 'Al menos un número', cumple: (v) => /\d/.test(v) },
  { clave: 'especial', texto: 'Al menos un carácter especial (!@#$%&*...)', cumple: (v) => /[^A-Za-z0-9ÑñÁÉÍÓÚáéíóú\s]/.test(v) },
  { clave: 'sinEspacios', texto: 'Sin espacios', cumple: (v) => v.length > 0 && !/\s/.test(v) },
];

// Devuelve un objeto con las reglas que no se cumplen, asi el checklist y el validador usan lo mismo
export const passwordSeguraValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const valor = (control.value ?? '') as string;
  if (!valor) return null;
  const errores: ValidationErrors = {};
  for (const regla of REGLAS_PASSWORD) {
    if (!regla.cumple(valor)) errores[regla.clave] = true;
  }
  return Object.keys(errores).length ? { passwordInsegura: errores } : null;
};

// Validador de grupo para que la confirmacion sea igual a la contrasena
export function passwordsCoincidenValidator(campo: string, confirmacion: string): ValidatorFn {
  return (grupo: AbstractControl): ValidationErrors | null => {
    const password = grupo.get(campo)?.value;
    const confirmar = grupo.get(confirmacion)?.value;
    if (!confirmar) return null;
    return password === confirmar ? null : { passwordsNoCoinciden: true };
  };
}

// Para cambio de contrasena: La nueva no puede ser igual a la actual
export function passwordDistintaValidator(campoActual: string, campoNueva: string): ValidatorFn {
  return (grupo: AbstractControl): ValidationErrors | null => {
    const actual = grupo.get(campoActual)?.value;
    const nueva = grupo.get(campoNueva)?.value;
    if (!actual || !nueva) return null;
    return actual === nueva ? { passwordRepetida: true } : null;
  };
}
