import { DatePipe } from '@angular/common';
import { Component, OnInit, computed, inject, input, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Tema } from '../../../models/tema.model';
import { AuthService } from '../../../services/auth.service';
import { ForoService } from '../../../services/foro.service';

// Temas publicados por el usuario logueado, desde aqui se editan o eliminan
@Component({
  selector: 'app-mis-temas',
  imports: [RouterLink, DatePipe],
  templateUrl: './mis-temas.html',
})
export class MisTemas implements OnInit {
  private readonly auth = inject(AuthService);
  protected readonly foro = inject(ForoService);

  readonly eliminado = input<string>();
  protected readonly mensaje = signal<string | null>(null);

  readonly temas = computed(() => {
    const usuario = this.auth.usuarioActual();
    return usuario ? this.foro.temasDeUsuario(usuario.idUsuario) : [];
  });
  readonly totalVisitas = computed(() => this.temas().reduce((suma, t) => suma + t.visitas, 0));

  ngOnInit(): void {
    if (this.eliminado()) this.mensaje.set('Tema eliminado correctamente.');
  }

  nombreCategoria(idCategoria: number): string {
    return this.foro.categoria(idCategoria)?.nombre ?? '';
  }

  eliminar(tema: Tema): void {
    if (confirm(`¿Seguro que quieres eliminar el tema "${tema.titulo}"? También se borrarán sus comentarios.`)) {
      this.foro.eliminarTema(tema.idTema);
      this.mensaje.set('Tema eliminado correctamente.');
    }
  }
}
