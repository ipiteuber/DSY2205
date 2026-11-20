import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RolBadge } from '../../components/rol-badge/rol-badge';

// Se muestra cuando el rol del usuario no tiene permiso para la ruta
@Component({
  selector: 'app-acceso-denegado',
  imports: [RouterLink, RolBadge],
  template: `
    <div class="container py-5">
      <div class="vacio text-center p-5 mx-auto" style="max-width: 560px">
        <div class="display-4 fw-bold text-danger mb-2">403</div>
        <h1 class="h4 fw-bold">Acceso denegado</h1>
        <p class="text-body-secondary">Tu rol actual (<app-rol-badge [rol]="auth.rolActual()" />) no tiene permiso para ver esta página.</p>
        <a class="btn btn-primary" routerLink="/inicio">Volver al inicio</a>
      </div>
    </div>
  `,
})
export class AccesoDenegado {
  protected readonly auth = inject(AuthService);
}
