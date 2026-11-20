// Roles del foro, mismos nombres que en la tabla ROLES
export type NombreRol = 'ADMIN' | 'MODERADOR' | 'USUARIO';

export interface Rol {
  idRol: number;
  nombre: NombreRol;
  descripcion: string;
}

export const ETIQUETAS_ROL: Record<NombreRol, string> = {
  ADMIN: 'Administrador',
  MODERADOR: 'Moderador',
  USUARIO: 'Usuario',
};
