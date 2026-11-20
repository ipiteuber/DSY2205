// Tema publicado dentro de una categoria
export type EstadoContenido = 'ACTIVO' | 'BANEADO';

export interface Tema {
  idTema: number;
  titulo: string;
  contenido: string;
  idCategoria: number;
  idUsuario: number;
  fechaCreacion: Date;
  estado: EstadoContenido;
  visitas: number;
}
