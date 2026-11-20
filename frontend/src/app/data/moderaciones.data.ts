import { Moderacion } from '../models/moderacion.model';

// Historial inicial de moderacion (tabla MODERACIONES)
export const MODERACIONES: Moderacion[] = [
  { idModeracion: 1, tipoContenido: 'TEMA', idContenido: 5, idModerador: 2, accion: 'BANEAR', idNorma: 2, motivo: 'Venta de cuentas premium', fecha: new Date('2026-10-14T15:00:00') },
  { idModeracion: 2, tipoContenido: 'COMENTARIO', idContenido: 4, idModerador: 2, accion: 'BANEAR', idNorma: 1, motivo: 'Comentario ofensivo hacia otros usuarios', fecha: new Date('2026-09-29T13:20:00') },
  { idModeracion: 3, tipoContenido: 'COMENTARIO', idContenido: 2, idModerador: 2, accion: 'BANEAR', idNorma: 1, motivo: 'Reportado como ofensivo', fecha: new Date('2026-09-03T12:00:00') },
  { idModeracion: 4, tipoContenido: 'COMENTARIO', idContenido: 2, idModerador: 1, accion: 'RESTAURAR', idNorma: null, motivo: 'Revisado por el administrador: No incumple las normas', fecha: new Date('2026-09-04T10:30:00') },
];
