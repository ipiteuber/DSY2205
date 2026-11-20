import { Component, ElementRef, inject, viewChild } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { RolBadge } from '../rol-badge/rol-badge';
import { AuthService } from '../../services/auth.service';

declare const bootstrap: any;

// Barra de navegacion, las opciones cambian segun el rol del usuario logueado
@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive, RolBadge],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private readonly router = inject(Router);
  protected readonly auth = inject(AuthService);

  private readonly menu = viewChild<ElementRef<HTMLElement>>('menu');

  // En celular el menu queda abierto despues de navegar, por eso se cierra a mano
  cerrarMenu(): void {
    const elemento = this.menu()?.nativeElement;
    if (elemento && typeof bootstrap !== 'undefined') {
      bootstrap.Collapse.getInstance(elemento)?.hide();
    }
  }

  cerrarSesion(): void {
    this.cerrarMenu();
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
