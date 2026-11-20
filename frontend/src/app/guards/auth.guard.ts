import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

// Guard que deja pasar solo si hay una sesion iniciada
export const authGuard: CanActivateFn = (_route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.estaLogueado()
    ? true
    : router.createUrlTree(['/login'], { queryParams: { volver: state.url } });
};
