import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TemaCard } from '../../components/tema-card/tema-card';
import { AuthService } from '../../services/auth.service';
import { ForoService } from '../../services/foro.service';
import { UsuarioService } from '../../services/usuario.service';

// Pagina principal con las categorias y los temas recientes y populares
@Component({
  selector: 'app-inicio',
  imports: [RouterLink, FormsModule, TemaCard],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css',
})
export class Inicio {
  private readonly router = inject(Router);
  protected readonly auth = inject(AuthService);
  protected readonly foro = inject(ForoService);
  private readonly usuarios = inject(UsuarioService);

  protected readonly textoBusqueda = signal('');

  readonly recientes = computed(() => this.foro.temasRecientes(6));
  readonly populares = computed(() => this.foro.temasPopulares(5));
  readonly estadisticas = computed(() => [
    { valor: this.foro.temasActivos().length, etiqueta: 'Temas' },
    { valor: this.foro.comentariosActivos().length, etiqueta: 'Comentarios' },
    { valor: this.usuarios.totalActivos(), etiqueta: 'Usuarios' },
  ]);

  contarTemas(idCategoria: number): number {
    return this.foro.contarTemasCategoria(idCategoria);
  }

  nombreCategoria(idCategoria: number): string {
    return this.foro.categoria(idCategoria)?.nombre ?? '';
  }

  buscar(): void {
    const q = this.textoBusqueda().trim();
    this.router.navigate(['/buscar'], { queryParams: q ? { q } : {} });
  }
}
