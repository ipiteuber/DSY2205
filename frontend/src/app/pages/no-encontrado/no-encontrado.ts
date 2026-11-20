import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

// Pagina 404
@Component({
  selector: 'app-no-encontrado',
  imports: [RouterLink],
  template: `
    <div class="container py-5">
      <div class="vacio text-center p-5 mx-auto" style="max-width: 560px">
        <div class="display-4 fw-bold texto-primario mb-2">404</div>
        <h1 class="h4 fw-bold">Página no encontrada</h1>
        <p class="text-body-secondary">La dirección que ingresaste no existe en ForoDuoc.</p>
        <a class="btn btn-primary" routerLink="/inicio">Volver al inicio</a>
      </div>
    </div>
  `,
})
export class NoEncontrado {}
