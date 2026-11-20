import { Injectable, computed, inject, signal } from '@angular/core';
import { MODERACIONES } from '../data/moderaciones.data';
import { NORMAS } from '../data/normas.data';
import { AccionModeracion, Moderacion, TipoContenido } from '../models/moderacion.model';
import { Norma } from '../models/norma.model';
import { AuthService, ResultadoOperacion } from './auth.service';
import { ForoService } from './foro.service';

// Servicio de moderacion, maneja las normas y el historial de baneos y restauraciones
@Injectable({ providedIn: 'root' })
export class ModeracionService {
  private readonly auth = inject(AuthService);
  private readonly foro = inject(ForoService);

  private readonly _moderaciones = signal<Moderacion[]>(MODERACIONES.map((m) => ({ ...m })));

  readonly normas: Norma[] = NORMAS;
  readonly historial = computed(() =>
    [...this._moderaciones()].sort((a, b) => b.fecha.getTime() - a.fecha.getTime()),
  );

  norma(idNorma: number | null): Norma | undefined {
    return this.normas.find((n) => n.idNorma === idNorma);
  }

  historialDe(tipo: TipoContenido, idContenido: number): Moderacion[] {
    return this.historial().filter((m) => m.tipoContenido === tipo && m.idContenido === idContenido);
  }

  banear(tipo: TipoContenido, idContenido: number, idNorma: number, motivo: string): ResultadoOperacion {
    if (!this.auth.puedeModerar()) {
      return { ok: false, mensaje: 'Error: No tienes permisos para moderar contenido.' };
    }
    if (!this.norma(idNorma)) {
      return { ok: false, mensaje: 'Error: Debes seleccionar una norma válida.' };
    }
    this.cambiarEstado(tipo, idContenido, 'BANEAR');
    this.registrar(tipo, idContenido, 'BANEAR', idNorma, motivo);
    return { ok: true, mensaje: tipo === 'TEMA' ? 'Tema baneado correctamente.' : 'Comentario baneado correctamente.' };
  }

  // Solo el administrador puede devolver contenido baneado
  restaurar(tipo: TipoContenido, idContenido: number, motivo: string): ResultadoOperacion {
    if (!this.auth.esAdmin()) {
      return { ok: false, mensaje: 'Error: Solo un administrador puede restaurar contenido.' };
    }
    this.cambiarEstado(tipo, idContenido, 'RESTAURAR');
    this.registrar(tipo, idContenido, 'RESTAURAR', null, motivo);
    return { ok: true, mensaje: tipo === 'TEMA' ? 'Tema restaurado correctamente.' : 'Comentario restaurado correctamente.' };
  }

  private cambiarEstado(tipo: TipoContenido, idContenido: number, accion: AccionModeracion): void {
    const estado = accion === 'BANEAR' ? 'BANEADO' : 'ACTIVO';
    if (tipo === 'TEMA') {
      this.foro.cambiarEstadoTema(idContenido, estado);
    } else {
      this.foro.cambiarEstadoComentario(idContenido, estado);
    }
  }

  private registrar(
    tipo: TipoContenido,
    idContenido: number,
    accion: AccionModeracion,
    idNorma: number | null,
    motivo: string,
  ): void {
    const registro: Moderacion = {
      idModeracion: Math.max(0, ...this._moderaciones().map((m) => m.idModeracion)) + 1,
      tipoContenido: tipo,
      idContenido,
      idModerador: this.auth.usuarioActual()!.idUsuario,
      accion,
      idNorma,
      motivo: motivo.trim(),
      fecha: new Date(),
    };
    this._moderaciones.update((lista) => [...lista, registro]);
  }
}
