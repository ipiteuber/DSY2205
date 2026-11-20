import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// Guard para login, registro y recuperar contrasena: Si ya hay sesion se va al inicio
export const invitadoGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.estaLogueado() ? router.createUrlTree(['/inicio']) : true;
};
