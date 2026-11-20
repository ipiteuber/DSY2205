import { Comentario } from '../models/comentario.model';

// Comentarios iniciales (tabla COMENTARIOS)
export const COMENTARIOS: Comentario[] = [
  { idComentario: 1, idTema: 1, idUsuario: 4, contenido: 'Yo partí con Python, la sintaxis es más simple para aprender lógica.', fecha: new Date('2026-09-02T14:02:00'), estado: 'ACTIVO' },
  { idComentario: 2, idTema: 1, idUsuario: 5, contenido: 'Java te obliga a ordenarte con los tipos, a mí me sirvió más.', fecha: new Date('2026-09-03T09:40:00'), estado: 'ACTIVO' },
  { idComentario: 3, idTema: 2, idUsuario: 3, contenido: 'Hollow Knight no es corto, pero Celeste se termina rápido y es buenísimo.', fecha: new Date('2026-09-16T10:15:00'), estado: 'ACTIVO' },
  { idComentario: 4, idTema: 3, idUsuario: 4, contenido: 'Este tema es para gente sin gusto musical.', fecha: new Date('2026-09-29T12:00:00'), estado: 'BANEADO' },
  { idComentario: 5, idTema: 4, idUsuario: 3, contenido: 'Dark completa, sin dudas.', fecha: new Date('2026-10-07T08:30:00'), estado: 'ACTIVO' },
  { idComentario: 6, idTema: 7, idUsuario: 5, contenido: 'Con chancaca y cáscara de naranja quedan mejores.', fecha: new Date('2026-11-04T11:10:00'), estado: 'ACTIVO' },
];
