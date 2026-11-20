import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from './components/footer/footer';
import { Navbar } from './components/navbar/navbar';

// Componente raiz: Navbar, contenido de la ruta y footer
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, Footer],
  template: `
    <div class="d-flex flex-column min-vh-100">
      <app-navbar />
      <main class="flex-grow-1">
        <router-outlet />
      </main>
      <app-footer />
    </div>
  `,
})
export class App {}
