import { Rol } from '../models/rol.model';

// Roles iniciales (tabla ROLES)
export const ROLES: Rol[] = [
  { idRol: 1, nombre: 'ADMIN', descripcion: 'Administra el foro: Banea y restaura contenido' },
  { idRol: 2, nombre: 'MODERADOR', descripcion: 'Revisa publicaciones y banea contenido que incumple las normas' },
  { idRol: 3, nombre: 'USUARIO', descripcion: 'Publica temas y comenta' },
];
