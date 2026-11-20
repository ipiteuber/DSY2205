import { EstadoContenido } from './tema.model';

// Comentario de un usuario en un tema
export interface Comentario {
  idComentario: number;
  idTema: number;
  idUsuario: number;
  contenido: string;
  fecha: Date;
  estado: EstadoContenido;
}
