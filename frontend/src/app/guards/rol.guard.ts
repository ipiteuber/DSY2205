import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { NombreRol } from '../models/rol.model';
import { AuthService } from '../services/auth.service';

// Guard por rol, los roles permitidos vienen en data.roles de la ruta
export const rolGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const roles = (route.data['roles'] ?? []) as NombreRol[];
  return auth.tieneRol(roles) ? true : router.createUrlTree(['/acceso-denegado']);
};
