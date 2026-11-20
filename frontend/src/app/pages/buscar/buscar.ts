import { Component, computed, effect, inject, input, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TemaCard } from '../../components/tema-card/tema-card';
import { ErrorCampo } from '../../components/error-campo/error-campo';
import { AuthService } from '../../services/auth.service';
import { ForoService } from '../../services/foro.service';

// Busqueda de temas por texto, opcionalmente filtrando por categoria
@Component({
  selector: 'app-buscar',
  imports: [ReactiveFormsModule, TemaCard, ErrorCampo],
  templateUrl: './buscar.html',
})
export class Buscar {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  protected readonly foro = inject(ForoService);

  readonly q = input<string>();

  readonly form = this.fb.nonNullable.group({
    texto: ['', [Validators.minLength(2), Validators.maxLength(100)]],
    categoria: [''],
  });

  private readonly valores = toSignal(this.form.valueChanges, { initialValue: this.form.getRawValue() });

  readonly textoValido = computed(() => {
    const texto = (this.valores().texto ?? '').trim();
    return texto.length >= 2 && texto.length <= 100;
  });

  readonly resultados = computed(() => {
    if (!this.textoValido()) return [];
    const { texto, categoria } = this.valores();
    return this.foro.buscarTemas(texto ?? '', categoria ? Number(categoria) : null, this.auth.puedeModerar());
  });

  constructor() {
    // Si llega ?q= desde el inicio se carga en el formulario
    effect(() => {
      const q = this.q() ?? '';
      untracked(() => {
        this.form.controls.texto.setValue(q);
        if (q) this.form.controls.texto.markAsTouched();
      });
    });
  }
}
