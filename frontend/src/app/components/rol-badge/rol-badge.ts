import { Component, computed, input } from '@angular/core';
import { ETIQUETAS_ROL, NombreRol } from '../../models/rol.model';

// Badge con el nombre del rol en espanol
@Component({
  selector: 'app-rol-badge',
  template: `<span class="badge rol-badge" [class]="'rol-' + (rol() ?? 'USUARIO').toLowerCase()">{{ etiqueta() }}</span>`,
})
export class RolBadge {
  readonly rol = input<NombreRol | null>(null);
  readonly etiqueta = computed(() => ETIQUETAS_ROL[this.rol() ?? 'USUARIO']);
}
