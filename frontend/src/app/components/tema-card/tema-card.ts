import { DatePipe } from '@angular/common';
import { Component, computed, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Tema } from '../../models/tema.model';
import { ForoService } from '../../services/foro.service';
import { UsuarioService } from '../../services/usuario.service';

// Tarjeta con el resumen de un tema, se usa en el inicio, categorias, busqueda y mis temas
@Component({
  selector: 'app-tema-card',
  imports: [RouterLink, DatePipe],
  templateUrl: './tema-card.html',
  styleUrl: './tema-card.css',
})
export class TemaCard {
  private readonly foro = inject(ForoService);
  private readonly usuarios = inject(UsuarioService);

  readonly tema = input.required<Tema>();
  readonly mostrarCategoria = input(true);

  readonly categoria = computed(() => this.foro.categoria(this.tema().idCategoria));
  readonly autor = computed(() => this.usuarios.nombreVisible(this.tema().idUsuario));
  readonly totalComentarios = computed(() => this.foro.contarComentarios(this.tema().idTema));
  readonly resumen = computed(() => {
    const texto = this.tema().contenido;
    return texto.length > 140 ? texto.slice(0, 140) + '…' : texto;
  });
}
