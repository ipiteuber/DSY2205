import { Component, computed, inject, input, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TemaCard } from '../../components/tema-card/tema-card';
import { Tema } from '../../models/tema.model';
import { AuthService } from '../../services/auth.service';
import { ForoService } from '../../services/foro.service';

type Orden = 'recientes' | 'visitas' | 'comentarios';

// Lista los temas de una categoria
@Component({
  selector: 'app-categoria',
  imports: [RouterLink, TemaCard],
  templateUrl: './categoria.html',
  styleUrl: './categoria.css',
})
export class CategoriaPage {
  private readonly foro = inject(ForoService);
  protected readonly auth = inject(AuthService);

  // Viene del parametro :id de la ruta
  readonly id = input.required<string>();
  protected readonly orden = signal<Orden>('recientes');

  readonly categoria = computed(() => this.foro.categoria(Number(this.id())));
  readonly temas = computed<Tema[]>(() => {
    const lista = this.foro.temasPorCategoria(Number(this.id()), this.auth.puedeModerar());
    switch (this.orden()) {
      case 'visitas':
        return [...lista].sort((a, b) => b.visitas - a.visitas);
      case 'comentarios':
        return [...lista].sort((a, b) => this.foro.contarComentarios(b.idTema) - this.foro.contarComentarios(a.idTema));
      default:
        return lista;
    }
  });

  cambiarOrden(valor: string): void {
    this.orden.set(valor as Orden);
  }
}
