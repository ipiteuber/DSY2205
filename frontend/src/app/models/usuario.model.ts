// Usuario registrado en el foro
export interface Usuario {
  idUsuario: number;
  nombreUsuario: string;
  email: string;
  password: string;
  nombreCompleto: string;
  idRol: number;
  activo: boolean;
  fechaRegistro: Date;
}
