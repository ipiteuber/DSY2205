import { Categoria } from '../models/categoria.model';

// Categorias iniciales (tabla CATEGORIAS)
export const CATEGORIAS: Categoria[] = [
  { idCategoria: 1, nombre: 'Tecnología', descripcion: 'Programación, hardware y novedades tecnológicas', imagen: 'img/categorias/tecnologia.svg', color: '#3B5BDB' },
  { idCategoria: 2, nombre: 'Videojuegos', descripcion: 'Juegos de PC, consolas y móviles', imagen: 'img/categorias/videojuegos.svg', color: '#7048E8' },
  { idCategoria: 3, nombre: 'Música', descripcion: 'Bandas, discos, conciertos e instrumentos', imagen: 'img/categorias/musica.svg', color: '#D6336C' },
  { idCategoria: 4, nombre: 'Cine y series', descripcion: 'Estrenos, recomendaciones y críticas', imagen: 'img/categorias/cine.svg', color: '#E8590C' },
  { idCategoria: 5, nombre: 'Deportes', descripcion: 'Fútbol, running y vida activa', imagen: 'img/categorias/deportes.svg', color: '#2B8A3E' },
  { idCategoria: 6, nombre: 'Cocina', descripcion: 'Recetas y datos para cocinar en casa', imagen: 'img/categorias/cocina.svg', color: '#C2850C' },
];
