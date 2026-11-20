// Registro del historial de moderacion
export type TipoContenido = 'TEMA' | 'COMENTARIO';
export type AccionModeracion = 'BANEAR' | 'RESTAURAR';

export interface Moderacion {
  idModeracion: number;
  tipoContenido: TipoContenido;
  idContenido: number;
  idModerador: number;
  accion: AccionModeracion;
  idNorma: number | null;
  motivo: string;
  fecha: Date;
}
