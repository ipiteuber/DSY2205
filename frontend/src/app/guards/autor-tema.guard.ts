import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ForoService } from '../services/foro.service';

// Guard para editar un tema: Solo lo puede editar quien lo publico
export const autorTemaGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const foro = inject(ForoService);
  const router = inject(Router);
  const tema = foro.tema(Number(route.paramMap.get('id')));
  if (!tema) return router.createUrlTree(['/no-encontrado']);
  return tema.idUsuario === auth.usuarioActual()?.idUsuario
    ? true
    : router.createUrlTree(['/acceso-denegado']);
};
