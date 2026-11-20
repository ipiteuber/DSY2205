import { Injectable, computed, inject, signal } from '@angular/core';
import { NombreRol } from '../models/rol.model';
import { Usuario } from '../models/usuario.model';
import { UsuarioService } from './usuario.service';

export interface ResultadoOperacion {
  ok: boolean;
  mensaje: string;
}

const CLAVE_SESION = 'foroduoc.idUsuario';

// Servicio de autenticacion, guarda el usuario logueado en un signal
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly usuarioService = inject(UsuarioService);

  private readonly idSesion = signal<number | null>(this.leerSesionGuardada());
  // Codigos de recuperacion generados por correo (simulado)
  private readonly codigosRecuperacion = new Map<string, string>();

  // Si el usuario se desactiva o se elimina, la sesion deja de ser valida
  readonly usuarioActual = computed<Usuario | null>(() => {
    const id = this.idSesion();
    if (id === null) return null;
    const usuario = this.usuarioService.usuarios().find((u) => u.idUsuario === id);
    return usuario && usuario.activo ? usuario : null;
  });

  readonly estaLogueado = computed(() => this.usuarioActual() !== null);
  readonly rolActual = computed<NombreRol | null>(() => {
    const usuario = this.usuarioActual();
    return usuario ? this.usuarioService.nombreRol(usuario.idRol) : null;
  });
  readonly esAdmin = computed(() => this.rolActual() === 'ADMIN');
  readonly puedeModerar = computed(() => this.rolActual() === 'ADMIN' || this.rolActual() === 'MODERADOR');

  login(credencial: string, password: string): ResultadoOperacion {
    const usuario = this.usuarioService.buscarPorCredencial(credencial);
    if (!usuario || usuario.password !== password) {
      return { ok: false, mensaje: 'Error: El usuario o la contraseña son incorrectos.' };
    }
    if (!usuario.activo) {
      return { ok: false, mensaje: 'Cuenta desactivada: Contacta al administrador del foro.' };
    }
    this.idSesion.set(usuario.idUsuario);
    this.guardarSesion(usuario.idUsuario);
    return { ok: true, mensaje: `Bienvenido/a, ${usuario.nombreCompleto}.` };
  }

  logout(): void {
    this.idSesion.set(null);
    this.guardarSesion(null);
  }

  tieneRol(roles: NombreRol[]): boolean {
    const rol = this.rolActual();
    return rol !== null && roles.includes(rol);
  }

  // Genera un codigo de 6 digitos que en la vida real llegaria por correo
  solicitarCodigo(email: string): ResultadoOperacion & { codigo?: string } {
    const usuario = this.usuarioService.buscarPorEmail(email);
    if (!usuario) {
      return { ok: false, mensaje: 'Error: No existe una cuenta asociada a ese correo.' };
    }
    if (!usuario.activo) {
      return { ok: false, mensaje: 'Cuenta desactivada: Contacta al administrador del foro.' };
    }
    const codigo = String(Math.floor(100000 + Math.random() * 900000));
    this.codigosRecuperacion.set(usuario.email, codigo);
    return { ok: true, mensaje: 'Código enviado.', codigo };
  }

  restablecerPassword(email: string, codigo: string, nuevaPassword: string): ResultadoOperacion {
    const usuario = this.usuarioService.buscarPorEmail(email);
    if (!usuario || this.codigosRecuperacion.get(usuario.email) !== codigo.trim()) {
      return { ok: false, mensaje: 'Error: El código ingresado no es válido.' };
    }
    if (usuario.password === nuevaPassword) {
      return { ok: false, mensaje: 'Error: La nueva contraseña debe ser distinta a la anterior.' };
    }
    this.usuarioService.cambiarPassword(usuario.idUsuario, nuevaPassword);
    this.codigosRecuperacion.delete(usuario.email);
    return { ok: true, mensaje: 'Contraseña actualizada: Ya puedes iniciar sesión.' };
  }

  private leerSesionGuardada(): number | null {
    try {
      const valor = localStorage.getItem(CLAVE_SESION);
      return valor ? Number(valor) : null;
    } catch {
      return null;
    }
  }

  private guardarSesion(idUsuario: number | null): void {
    try {
      if (idUsuario === null) {
        localStorage.removeItem(CLAVE_SESION);
      } else {
        localStorage.setItem(CLAVE_SESION, String(idUsuario));
      }
    } catch {
      // Si el navegador bloquea el storage la sesion solo dura hasta recargar
    }
  }
}
