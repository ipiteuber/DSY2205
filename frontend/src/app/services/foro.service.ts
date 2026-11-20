import { Injectable, computed, signal } from '@angular/core';
import { CATEGORIAS } from '../data/categorias.data';
import { COMENTARIOS } from '../data/comentarios.data';
import { TEMAS } from '../data/temas.data';
import { Categoria } from '../models/categoria.model';
import { Comentario } from '../models/comentario.model';
import { EstadoContenido, Tema } from '../models/tema.model';

export interface DatosTema {
  titulo: string;
  contenido: string;
  idCategoria: number;
}

// Servicio del foro, guarda en memoria las categorias, los temas y los comentarios
@Injectable({ providedIn: 'root' })
export class ForoService {
  private readonly _temas = signal<Tema[]>(TEMAS.map((t) => ({ ...t })));
  private readonly _comentarios = signal<Comentario[]>(COMENTARIOS.map((c) => ({ ...c })));

  readonly categorias: Categoria[] = CATEGORIAS;
  readonly temas = this._temas.asReadonly();
  readonly comentarios = this._comentarios.asReadonly();

  readonly temasActivos = computed(() => this._temas().filter((t) => t.estado === 'ACTIVO'));
  readonly comentariosActivos = computed(() =>
    this._comentarios().filter((c) => c.estado === 'ACTIVO'),
  );

  categoria(idCategoria: number): Categoria | undefined {
    return this.categorias.find((c) => c.idCategoria === idCategoria);
  }

  tema(idTema: number): Tema | undefined {
    return this._temas().find((t) => t.idTema === idTema);
  }

  comentario(idComentario: number): Comentario | undefined {
    return this._comentarios().find((c) => c.idComentario === idComentario);
  }

  // Los moderadores ven tambien lo baneado, el resto solo lo activo
  temasVisibles(incluirBaneados: boolean): Tema[] {
    return incluirBaneados ? this._temas() : this.temasActivos();
  }

  temasPorCategoria(idCategoria: number, incluirBaneados: boolean): Tema[] {
    return this.ordenarPorFecha(
      this.temasVisibles(incluirBaneados).filter((t) => t.idCategoria === idCategoria),
    );
  }

  temasDeUsuario(idUsuario: number): Tema[] {
    return this.ordenarPorFecha(this._temas().filter((t) => t.idUsuario === idUsuario));
  }

  temasRecientes(cantidad: number): Tema[] {
    return this.ordenarPorFecha(this.temasActivos()).slice(0, cantidad);
  }

  temasPopulares(cantidad: number): Tema[] {
    return [...this.temasActivos()].sort((a, b) => b.visitas - a.visitas).slice(0, cantidad);
  }

  buscarTemas(texto: string, idCategoria: number | null, incluirBaneados: boolean): Tema[] {
    const termino = this.normalizar(texto);
    return this.ordenarPorFecha(
      this.temasVisibles(incluirBaneados).filter(
        (t) =>
          (idCategoria === null || t.idCategoria === idCategoria) &&
          (this.normalizar(t.titulo).includes(termino) || this.normalizar(t.contenido).includes(termino)),
      ),
    );
  }

  comentariosDeTema(idTema: number, incluirBaneados: boolean): Comentario[] {
    return this._comentarios()
      .filter((c) => c.idTema === idTema && (incluirBaneados || c.estado === 'ACTIVO'))
      .sort((a, b) => a.fecha.getTime() - b.fecha.getTime());
  }

  contarComentarios(idTema: number): number {
    return this.comentariosActivos().filter((c) => c.idTema === idTema).length;
  }

  contarTemasCategoria(idCategoria: number): number {
    return this.temasActivos().filter((t) => t.idCategoria === idCategoria).length;
  }

  registrarVisita(idTema: number): void {
    this._temas.update((lista) =>
      lista.map((t) => (t.idTema === idTema ? { ...t, visitas: t.visitas + 1 } : t)),
    );
  }

  crearTema(datos: DatosTema, idUsuario: number): Tema {
    const nuevo: Tema = {
      idTema: Math.max(0, ...this._temas().map((t) => t.idTema)) + 1,
      titulo: datos.titulo.trim(),
      contenido: datos.contenido.trim(),
      idCategoria: datos.idCategoria,
      idUsuario,
      fechaCreacion: new Date(),
      estado: 'ACTIVO',
      visitas: 0,
    };
    this._temas.update((lista) => [...lista, nuevo]);
    return nuevo;
  }

  actualizarTema(idTema: number, datos: DatosTema): void {
    this._temas.update((lista) =>
      lista.map((t) =>
        t.idTema === idTema
          ? { ...t, titulo: datos.titulo.trim(), contenido: datos.contenido.trim(), idCategoria: datos.idCategoria }
          : t,
      ),
    );
  }

  // Igual que el ON DELETE CASCADE de la base, se borran tambien sus comentarios
  eliminarTema(idTema: number): void {
    this._temas.update((lista) => lista.filter((t) => t.idTema !== idTema));
    this._comentarios.update((lista) => lista.filter((c) => c.idTema !== idTema));
  }

  agregarComentario(idTema: number, idUsuario: number, contenido: string): Comentario {
    const nuevo: Comentario = {
      idComentario: Math.max(0, ...this._comentarios().map((c) => c.idComentario)) + 1,
      idTema,
      idUsuario,
      contenido: contenido.trim(),
      fecha: new Date(),
      estado: 'ACTIVO',
    };
    this._comentarios.update((lista) => [...lista, nuevo]);
    return nuevo;
  }

  eliminarComentario(idComentario: number): void {
    this._comentarios.update((lista) => lista.filter((c) => c.idComentario !== idComentario));
  }

  cambiarEstadoTema(idTema: number, estado: EstadoContenido): void {
    this._temas.update((lista) => lista.map((t) => (t.idTema === idTema ? { ...t, estado } : t)));
  }

  cambiarEstadoComentario(idComentario: number, estado: EstadoContenido): void {
    this._comentarios.update((lista) =>
      lista.map((c) => (c.idComentario === idComentario ? { ...c, estado } : c)),
    );
  }

  private ordenarPorFecha(temas: Tema[]): Tema[] {
    return [...temas].sort((a, b) => b.fechaCreacion.getTime() - a.fechaCreacion.getTime());
  }

  // Quita tildes y mayusculas para que la busqueda sea mas flexible
  private normalizar(texto: string): string {
    return texto.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase().trim();
  }
}
