import { Component, OnInit, inject, input, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AccionModeracion, TipoContenido } from '../../models/moderacion.model';
import { ModeracionService } from '../../services/moderacion.service';
import { noSoloEspaciosValidator } from '../../validators/texto.validators';
import { ErrorCampo } from '../error-campo/error-campo';

// Ventana para banear o restaurar un tema o comentario indicando la norma y el motivo
@Component({
  selector: 'app-modal-moderacion',
  imports: [ReactiveFormsModule, ErrorCampo],
  templateUrl: './modal-moderacion.html',
})
export class ModalModeracion implements OnInit {
  private readonly fb = inject(FormBuilder);
  protected readonly moderacion = inject(ModeracionService);

  readonly tipo = input.required<TipoContenido>();
  readonly idContenido = input.required<number>();
  readonly accion = input.required<AccionModeracion>();
  readonly descripcion = input('');

  readonly cerrar = output<string | null>();

  protected readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    idNorma: ['', Validators.required],
    motivo: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(200), noSoloEspaciosValidator]],
  });

  ngOnInit(): void {
    // Para restaurar no se pide norma, igual que en el backend
    if (this.accion() === 'RESTAURAR') {
      this.form.controls.idNorma.clearValidators();
      this.form.controls.idNorma.updateValueAndValidity();
    }
  }

  confirmar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { idNorma, motivo } = this.form.getRawValue();
    const resultado =
      this.accion() === 'BANEAR'
        ? this.moderacion.banear(this.tipo(), this.idContenido(), Number(idNorma), motivo)
        : this.moderacion.restaurar(this.tipo(), this.idContenido(), motivo);
    if (resultado.ok) {
      this.cerrar.emit(resultado.mensaje);
    } else {
      this.error.set(resultado.mensaje);
    }
  }
}
