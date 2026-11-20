import { DatePipe } from '@angular/common';
import { Component, computed, effect, inject, input, signal, untracked } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ErrorCampo } from '../../../components/error-campo/error-campo';
import { ModalModeracion } from '../../../components/modal-moderacion/modal-moderacion';
import { RolBadge } from '../../../components/rol-badge/rol-badge';
import { Comentario } from '../../../models/comentario.model';
import { AccionModeracion, TipoContenido } from '../../../models/moderacion.model';
import { AuthService } from '../../../services/auth.service';
import { ForoService } from '../../../services/foro.service';
import { UsuarioService } from '../../../services/usuario.service';
import { noSoloEspaciosValidator } from '../../../validators/texto.validators';

interface AccionPendiente {
  tipo: TipoContenido;
  idContenido: number;
  accion: AccionModeracion;
  descripcion: string;
}

// Detalle de un tema con sus comentarios, cada vez que se abre suma una visita
@Component({
  selector: 'app-tema-detalle',
  imports: [RouterLink, DatePipe, ReactiveFormsModule, ErrorCampo, ModalModeracion, RolBadge],
  templateUrl: './tema-detalle.html',
  styleUrl: './tema-detalle.css',
})
export class TemaDetalle {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly foro = inject(ForoService);
  private readonly usuarios = inject(UsuarioService);
  protected readonly auth = inject(AuthService);

  readonly id = input.required<string>();

  protected readonly mensaje = signal<string | null>(null);
  protected readonly pendiente = signal<AccionPendiente | null>(null);

  readonly tema = computed(() => this.foro.tema(Number(this.id())));
  readonly categoria = computed(() => {
    const tema = this.tema();
    return tema ? this.foro.categoria(tema.idCategoria) : undefined;
  });
  // Un usuario normal no puede ver un tema baneado
  readonly visible = computed(() => {
    const tema = this.tema();
    return !!tema && (tema.estado === 'ACTIVO' || this.auth.puedeModerar());
  });
  readonly esAutor = computed(() => this.tema()?.idUsuario === this.auth.usuarioActual()?.idUsuario);
  readonly comentarios = computed(() =>
    this.tema() ? this.foro.comentariosDeTema(this.tema()!.idTema, this.auth.puedeModerar()) : [],
  );

  readonly form = this.fb.nonNullable.group({
    contenido: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(500), noSoloEspaciosValidator]],
  });

  constructor() {
    effect(() => {
      const idTema = Number(this.id());
      untracked(() => {
        const tema = this.foro.tema(idTema);
        if (tema && (tema.estado === 'ACTIVO' || this.auth.puedeModerar())) {
          this.foro.registrarVisita(idTema);
        }
      });
    });
  }

  autor(idUsuario: number): string {
    return this.usuarios.nombreVisible(idUsuario);
  }

  rolAutor(idUsuario: number) {
    const usuario = this.usuarios.buscarPorId(idUsuario);
    return usuario ? this.usuarios.nombreRol(usuario.idRol) : null;
  }

  esMio(comentario: Comentario): boolean {
    return comentario.idUsuario === this.auth.usuarioActual()?.idUsuario;
  }

  comentar(): void {
    const tema = this.tema();
    const usuario = this.auth.usuarioActual();
    if (!tema || !usuario || this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.foro.agregarComentario(tema.idTema, usuario.idUsuario, this.form.getRawValue().contenido);
    this.form.reset();
    this.mensaje.set('Comentario publicado correctamente.');
  }

  eliminarComentario(comentario: Comentario): void {
    if (confirm('¿Seguro que quieres eliminar tu comentario?')) {
      this.foro.eliminarComentario(comentario.idComentario);
      this.mensaje.set('Comentario eliminado.');
    }
  }

  eliminarTema(): void {
    const tema = this.tema();
    if (!tema) return;
    if (confirm(`¿Seguro que quieres eliminar el tema "${tema.titulo}"? También se borrarán sus comentarios.`)) {
      this.foro.eliminarTema(tema.idTema);
      this.router.navigate(['/mis-temas'], { queryParams: { eliminado: 1 } });
    }
  }

  abrirModeracion(tipo: TipoContenido, idContenido: number, accion: AccionModeracion, descripcion: string): void {
    this.pendiente.set({ tipo, idContenido, accion, descripcion });
  }

  cerrarModeracion(resultado: string | null): void {
    this.pendiente.set(null);
    if (resultado) this.mensaje.set(resultado);
  }
}
