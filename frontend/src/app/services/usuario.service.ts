import { Injectable, computed, signal } from '@angular/core';
import { ROLES } from '../data/roles.data';
import { USUARIOS } from '../data/usuarios.data';
import { NombreRol, Rol } from '../models/rol.model';
import { Usuario } from '../models/usuario.model';

export interface DatosRegistro {
  nombreCompleto: string;
  nombreUsuario: string;
  email: string;
  password: string;
}

// Servicio de usuarios, funciona como repositorio en memoria de usuarios y roles
@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly _usuarios = signal<Usuario[]>(USUARIOS.map((u) => ({ ...u })));

  readonly usuarios = this._usuarios.asReadonly();
  readonly roles: Rol[] = ROLES;
  readonly totalActivos = computed(() => this._usuarios().filter((u) => u.activo).length);

  buscarPorId(idUsuario: number): Usuario | undefined {
    return this._usuarios().find((u) => u.idUsuario === idUsuario);
  }

  // Sirve para iniciar sesion con el nombre de usuario o con el correo
  buscarPorCredencial(valor: string): Usuario | undefined {
    const texto = valor.trim().toLowerCase();
    return this._usuarios().find(
      (u) => u.nombreUsuario.toLowerCase() === texto || u.email.toLowerCase() === texto,
    );
  }

  buscarPorEmail(email: string): Usuario | undefined {
    const texto = email.trim().toLowerCase();
    return this._usuarios().find((u) => u.email.toLowerCase() === texto);
  }

  existeNombreUsuario(nombreUsuario: string, excluirId?: number): boolean {
    const texto = nombreUsuario.trim().toLowerCase();
    return this._usuarios().some(
      (u) => u.nombreUsuario.toLowerCase() === texto && u.idUsuario !== excluirId,
    );
  }

  existeEmail(email: string, excluirId?: number): boolean {
    const texto = email.trim().toLowerCase();
    return this._usuarios().some((u) => u.email.toLowerCase() === texto && u.idUsuario !== excluirId);
  }

  rolDe(usuario: Usuario | null | undefined): Rol | undefined {
    return usuario ? this.roles.find((r) => r.idRol === usuario.idRol) : undefined;
  }

  nombreRol(idRol: number): NombreRol {
    return this.roles.find((r) => r.idRol === idRol)?.nombre ?? 'USUARIO';
  }

  nombreVisible(idUsuario: number): string {
    return this.buscarPorId(idUsuario)?.nombreUsuario ?? 'Usuario eliminado';
  }

  registrar(datos: DatosRegistro): Usuario {
    const idRolUsuario = this.roles.find((r) => r.nombre === 'USUARIO')!.idRol;
    const nuevo: Usuario = {
      idUsuario: this.siguienteId(),
      nombreUsuario: datos.nombreUsuario.trim(),
      email: datos.email.trim().toLowerCase(),
      password: datos.password,
      nombreCompleto: datos.nombreCompleto.trim(),
      idRol: idRolUsuario,
      activo: true,
      fechaRegistro: new Date(),
    };
    this._usuarios.update((lista) => [...lista, nuevo]);
    return nuevo;
  }

  actualizarPerfil(idUsuario: number, nombreCompleto: string, email: string): void {
    this.modificar(idUsuario, { nombreCompleto: nombreCompleto.trim(), email: email.trim().toLowerCase() });
  }

  cambiarPassword(idUsuario: number, nuevaPassword: string): void {
    this.modificar(idUsuario, { password: nuevaPassword });
  }

  cambiarRol(idUsuario: number, idRol: number): void {
    this.modificar(idUsuario, { idRol });
  }

  cambiarEstado(idUsuario: number, activo: boolean): void {
    this.modificar(idUsuario, { activo });
  }

  eliminar(idUsuario: number): void {
    this._usuarios.update((lista) => lista.filter((u) => u.idUsuario !== idUsuario));
  }

  private modificar(idUsuario: number, cambios: Partial<Usuario>): void {
    this._usuarios.update((lista) =>
      lista.map((u) => (u.idUsuario === idUsuario ? { ...u, ...cambios } : u)),
    );
  }

  private siguienteId(): number {
    return Math.max(0, ...this._usuarios().map((u) => u.idUsuario)) + 1;
  }
}
