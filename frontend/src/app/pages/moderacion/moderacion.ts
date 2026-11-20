import { DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ModalModeracion } from '../../components/modal-moderacion/modal-moderacion';
import { AccionModeracion, Moderacion, TipoContenido } from '../../models/moderacion.model';
import { EstadoContenido } from '../../models/tema.model';
import { AuthService } from '../../services/auth.service';
import { ForoService } from '../../services/foro.service';
import { ModeracionService } from '../../services/moderacion.service';
import { UsuarioService } from '../../services/usuario.service';

type Pestana = 'temas' | 'comentarios' | 'historial' | 'normas';
type FiltroEstado = 'TODOS' | EstadoContenido;

interface AccionPendiente {
  tipo: TipoContenido;
  idContenido: number;
  accion: AccionModeracion;
  descripcion: string;
}

// Panel de moderacion para ADMIN y MODERADOR
@Component({
  selector: 'app-moderacion',
  imports: [RouterLink, DatePipe, ModalModeracion],
  templateUrl: './moderacion.html',
})
export class ModeracionPage {
  private readonly usuarios = inject(UsuarioService);
  protected readonly auth = inject(AuthService);
  protected readonly foro = inject(ForoService);
  protected readonly moderacion = inject(ModeracionService);

  protected readonly pestana = signal<Pestana>('temas');
  protected readonly filtro = signal<FiltroEstado>('TODOS');
  protected readonly mensaje = signal<string | null>(null);
  protected readonly pendiente = signal<AccionPendiente | null>(null);

  readonly pestanas: { id: Pestana; texto: string }[] = [
    { id: 'temas', texto: 'Temas' },
    { id: 'comentarios', texto: 'Comentarios' },
    { id: 'historial', texto: 'Historial' },
    { id: 'normas', texto: 'Normas' },
  ];

  readonly temas = computed(() =>
    [...this.foro.temas()]
      .filter((t) => this.filtro() === 'TODOS' || t.estado === this.filtro())
      .sort((a, b) => b.fechaCreacion.getTime() - a.fechaCreacion.getTime()),
  );
  readonly comentarios = computed(() =>
    [...this.foro.comentarios()]
      .filter((c) => this.filtro() === 'TODOS' || c.estado === this.filtro())
      .sort((a, b) => b.fecha.getTime() - a.fecha.getTime()),
  );
  readonly totalBaneados = computed(
    () =>
      this.foro.temas().filter((t) => t.estado === 'BANEADO').length +
      this.foro.comentarios().filter((c) => c.estado === 'BANEADO').length,
  );

  cambiarPestana(pestana: Pestana): void {
    this.pestana.set(pestana);
    this.mensaje.set(null);
  }

  cambiarFiltro(valor: string): void {
    this.filtro.set(valor as FiltroEstado);
  }

  autor(idUsuario: number): string {
    return this.usuarios.nombreVisible(idUsuario);
  }

  tituloTema(idTema: number): string {
    return this.foro.tema(idTema)?.titulo ?? 'Tema eliminado';
  }

  // Texto corto del contenido moderado para mostrar en el historial
  referencia(registro: Moderacion): string {
    if (registro.tipoContenido === 'TEMA') {
      return this.foro.tema(registro.idContenido)?.titulo ?? `Tema #${registro.idContenido} (eliminado)`;
    }
    const comentario = this.foro.comentario(registro.idContenido);
    return comentario ? this.recortar(comentario.contenido, 60) : `Comentario #${registro.idContenido} (eliminado)`;
  }

  recortar(texto: string, largo: number): string {
    return texto.length > largo ? texto.slice(0, largo) + '…' : texto;
  }

  abrir(tipo: TipoContenido, idContenido: number, accion: AccionModeracion, descripcion: string): void {
    this.pendiente.set({ tipo, idContenido, accion, descripcion });
  }

  cerrar(resultado: string | null): void {
    this.pendiente.set(null);
    if (resultado) this.mensaje.set(resultado);
  }
}
