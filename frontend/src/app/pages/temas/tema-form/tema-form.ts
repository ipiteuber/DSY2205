import { Component, OnInit, computed, inject, input } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ErrorCampo } from '../../../components/error-campo/error-campo';
import { AuthService } from '../../../services/auth.service';
import { ForoService } from '../../../services/foro.service';
import { noSoloEspaciosValidator } from '../../../validators/texto.validators';

// Formulario para publicar un tema nuevo o editar uno propio
@Component({
  selector: 'app-tema-form',
  imports: [ReactiveFormsModule, RouterLink, ErrorCampo],
  templateUrl: './tema-form.html',
})
export class TemaForm implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  protected readonly foro = inject(ForoService);

  // id viene de la ruta de edicion y categoria de ?categoria= al publicar
  readonly id = input<string>();
  readonly categoria = input<string>();

  readonly esEdicion = computed(() => !!this.id());

  readonly form = this.fb.nonNullable.group({
    idCategoria: ['', Validators.required],
    titulo: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(100), noSoloEspaciosValidator]],
    contenido: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(2000), noSoloEspaciosValidator]],
  });

  ngOnInit(): void {
    const id = this.id();
    if (id) {
      const tema = this.foro.tema(Number(id));
      if (tema) {
        this.form.setValue({ idCategoria: String(tema.idCategoria), titulo: tema.titulo, contenido: tema.contenido });
      }
    } else if (this.categoria() && this.foro.categoria(Number(this.categoria()))) {
      this.form.controls.idCategoria.setValue(this.categoria()!);
    }
  }

  guardar(): void {
    const usuario = this.auth.usuarioActual();
    if (this.form.invalid || !usuario) {
      this.form.markAllAsTouched();
      return;
    }
    const { idCategoria, titulo, contenido } = this.form.getRawValue();
    const datos = { idCategoria: Number(idCategoria), titulo, contenido };
    const id = this.id();
    if (id) {
      this.foro.actualizarTema(Number(id), datos);
      this.router.navigate(['/temas', id]);
    } else {
      const nuevo = this.foro.crearTema(datos, usuario.idUsuario);
      this.router.navigate(['/temas', nuevo.idTema]);
    }
  }

  cancelar(): void {
    const id = this.id();
    this.router.navigate(id ? ['/temas', id] : ['/inicio']);
  }
}
