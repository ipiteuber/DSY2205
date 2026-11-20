import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { autorTemaGuard } from './guards/autor-tema.guard';
import { invitadoGuard } from './guards/invitado.guard';
import { rolGuard } from './guards/rol.guard';
import { UsuariosAdmin } from './pages/admin/usuarios/usuarios';
import { AccesoDenegado } from './pages/acceso-denegado/acceso-denegado';
import { Login } from './pages/auth/login/login';
import { RecuperarPassword } from './pages/auth/recuperar-password/recuperar-password';
import { Registro } from './pages/auth/registro/registro';
import { Buscar } from './pages/buscar/buscar';
import { CategoriaPage } from './pages/categoria/categoria';
import { Inicio } from './pages/inicio/inicio';
import { ModeracionPage } from './pages/moderacion/moderacion';
import { NoEncontrado } from './pages/no-encontrado/no-encontrado';
import { Perfil } from './pages/perfil/perfil';
import { MisTemas } from './pages/temas/mis-temas/mis-temas';
import { TemaDetalle } from './pages/temas/tema-detalle/tema-detalle';
import { TemaForm } from './pages/temas/tema-form/tema-form';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },

  // Publicas, solo sin sesion
  { path: 'login', component: Login, canActivate: [invitadoGuard], title: 'Inicio de sesión | ForoDuoc' },
  { path: 'registro', component: Registro, canActivate: [invitadoGuard], title: 'Registro | ForoDuoc' },
  { path: 'recuperar-contrasena', component: RecuperarPassword, canActivate: [invitadoGuard], title: 'Recuperar contraseña | ForoDuoc' },

  // Cualquier usuario logueado
  { path: 'inicio', component: Inicio, canActivate: [authGuard], title: 'Inicio | ForoDuoc' },
  { path: 'categorias/:id', component: CategoriaPage, canActivate: [authGuard], title: 'Categoría | ForoDuoc' },
  { path: 'buscar', component: Buscar, canActivate: [authGuard], title: 'Buscar temas | ForoDuoc' },
  { path: 'temas/nuevo', component: TemaForm, canActivate: [authGuard], title: 'Publicar tema | ForoDuoc' },
  { path: 'temas/:id', component: TemaDetalle, canActivate: [authGuard], title: 'Tema | ForoDuoc' },
  { path: 'temas/:id/editar', component: TemaForm, canActivate: [authGuard, autorTemaGuard], title: 'Editar tema | ForoDuoc' },
  { path: 'mis-temas', component: MisTemas, canActivate: [authGuard], title: 'Mis temas | ForoDuoc' },
  { path: 'perfil', component: Perfil, canActivate: [authGuard], title: 'Mi perfil | ForoDuoc' },

  // Por rol
  {
    path: 'moderacion',
    component: ModeracionPage,
    canActivate: [authGuard, rolGuard],
    data: { roles: ['ADMIN', 'MODERADOR'] },
    title: 'Moderación | ForoDuoc',
  },
  {
    path: 'admin/usuarios',
    component: UsuariosAdmin,
    canActivate: [authGuard, rolGuard],
    data: { roles: ['ADMIN'] },
    title: 'Usuarios | ForoDuoc',
  },

  { path: 'acceso-denegado', component: AccesoDenegado, canActivate: [authGuard], title: 'Acceso denegado | ForoDuoc' },
  { path: 'no-encontrado', component: NoEncontrado, title: 'Página no encontrada | ForoDuoc' },
  { path: '**', component: NoEncontrado, title: 'Página no encontrada | ForoDuoc' },
];
