import { DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { RolBadge } from '../../../components/rol-badge/rol-badge';
import { ETIQUETAS_ROL } from '../../../models/rol.model';
import { Usuario } from '../../../models/usuario.model';
import { AuthService } from '../../../services/auth.service';
import { UsuarioService } from '../../../services/usuario.service';

// Administracion de usuarios, solo para ADMIN
@Component({
  selector: 'app-usuarios',
  imports: [DatePipe, RolBadge],
  templateUrl: './usuarios.html',
})
export class UsuariosAdmin {
  private readonly auth = inject(AuthService);
  protected readonly usuarioService = inject(UsuarioService);

  protected readonly etiquetas = ETIQUETAS_ROL;
  protected readonly texto = signal('');
  protected readonly filtroRol = signal<number | null>(null);
  protected readonly mensaje = signal<string | null>(null);

  readonly lista = computed(() => {
    const texto = this.texto().trim().toLowerCase();
    const rol = this.filtroRol();
    return this.usuarioService
      .usuarios()
      .filter(
        (u) =>
          (rol === null || u.idRol === rol) &&
          (!texto ||
            u.nombreUsuario.toLowerCase().includes(texto) ||
            u.nombreCompleto.toLowerCase().includes(texto) ||
            u.email.toLowerCase().includes(texto)),
      );
  });

  readonly resumen = computed(() =>
    this.usuarioService.roles.map((r) => ({
      rol: r.nombre,
      total: this.usuarioService.usuarios().filter((u) => u.idRol === r.idRol).length,
    })),
  );

  // El admin no puede quitarse el rol, desactivarse ni eliminarse a si mismo
  esYo(usuario: Usuario): boolean {
    return usuario.idUsuario === this.auth.usuarioActual()?.idUsuario;
  }

  rol(usuario: Usuario) {
    return this.usuarioService.nombreRol(usuario.idRol);
  }

  buscar(valor: string): void {
    this.texto.set(valor.slice(0, 100));
  }

  filtrarRol(valor: string): void {
    this.filtroRol.set(valor ? Number(valor) : null);
  }

  cambiarRol(usuario: Usuario, valor: string): void {
    const idRol = Number(valor);
    if (this.esYo(usuario) || idRol === usuario.idRol) return;
    this.usuarioService.cambiarRol(usuario.idUsuario, idRol);
    const nombre = ETIQUETAS_ROL[this.usuarioService.nombreRol(idRol)];
    this.mensaje.set(`Rol actualizado: El usuario ${usuario.nombreUsuario} ahora es ${nombre}.`);
  }

  cambiarEstado(usuario: Usuario): void {
    if (this.esYo(usuario)) return;
    this.usuarioService.cambiarEstado(usuario.idUsuario, !usuario.activo);
    this.mensaje.set(
      usuario.activo
        ? `Usuario desactivado: La cuenta ${usuario.nombreUsuario} ya no podrá iniciar sesión.`
        : `Usuario activado: La cuenta ${usuario.nombreUsuario} puede volver a iniciar sesión.`,
    );
  }

  eliminar(usuario: Usuario): void {
    if (this.esYo(usuario)) return;
    if (confirm(`¿Seguro que quieres eliminar a ${usuario.nombreUsuario}? Esta acción no se puede deshacer.`)) {
      this.usuarioService.eliminar(usuario.idUsuario);
      this.mensaje.set(`Usuario eliminado: La cuenta ${usuario.nombreUsuario} fue eliminada del foro.`);
    }
  }
}
