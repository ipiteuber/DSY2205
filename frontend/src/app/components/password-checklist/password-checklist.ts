import { Component, computed, input } from '@angular/core';
import { REGLAS_PASSWORD } from '../../validators/password.validators';

// Lista en vivo de las reglas de la contrasena, marca las que ya se cumplen
@Component({
  selector: 'app-password-checklist',
  templateUrl: './password-checklist.html',
  styleUrl: './password-checklist.css',
})
export class PasswordChecklist {
  readonly valor = input<string | null>('');

  readonly reglas = computed(() => {
    const valor = this.valor() ?? '';
    return REGLAS_PASSWORD.map((r) => ({ texto: r.texto, cumple: r.cumple(valor) }));
  });
  readonly cumplidas = computed(() => this.reglas().filter((r) => r.cumple).length);
}
