package com.duoc.foro.service;

import com.duoc.foro.client.UsuarioClient;
import com.duoc.foro.dto.TemaDetalle;
import com.duoc.foro.dto.TemaRequest;
import com.duoc.foro.exception.AccesoDenegadoException;
import com.duoc.foro.exception.RecursoNoEncontradoException;
import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.model.Tema;
import com.duoc.foro.repository.ComentarioRepository;
import com.duoc.foro.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Servicio de temas, maneja publicar, buscar, editar y el estado ACTIVO / BANEADO
@Service
@RequiredArgsConstructor
public class TemaService {

    private final TemaRepository temaRepository;
    private final ComentarioRepository comentarioRepository;
    private final CategoriaService categoriaService;
    private final UsuarioClient usuarioClient;

    public List<Tema> listarActivos() {
        return temaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoContenido.ACTIVO);
    }

    public List<Tema> listarBaneados() {
        return temaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoContenido.BANEADO);
    }

    public Tema buscarPorId(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con ID " + id));
    }

    @Transactional
    public TemaDetalle verDetalle(Long id) {
        Tema tema = buscarPorId(id);
        if (tema.getEstado() == EstadoContenido.BANEADO) {
            throw new AccesoDenegadoException("El tema " + id + " fue baneado por incumplir las normas del foro");
        }
        tema.setVisitas(tema.getVisitas() + 1);
        List<Comentario> comentarios = comentarioRepository.findByTema_IdAndEstadoOrderByFechaAsc(id, EstadoContenido.ACTIVO);
        return new TemaDetalle(tema, comentarios.size(), comentarios);
    }

    public List<Tema> buscarPorCategoria(Long idCategoria) {
        categoriaService.buscarPorId(idCategoria);
        return temaRepository.findByCategoria_IdAndEstadoOrderByFechaCreacionDesc(idCategoria, EstadoContenido.ACTIVO);
    }

    public List<Tema> buscarPorTexto(String texto) {
        return temaRepository.buscarPorTexto(texto, EstadoContenido.ACTIVO);
    }

    public List<Tema> buscarPorUsuario(Long idUsuario) {
        return temaRepository.findByIdUsuarioOrderByFechaCreacionDesc(idUsuario);
    }

    public List<Tema> populares() {
        return temaRepository.findTop5ByEstadoOrderByVisitasDesc(EstadoContenido.ACTIVO);
    }

    public Tema publicar(TemaRequest datos) {
        usuarioClient.validarUsuarioActivo(datos.idUsuario());
        Tema tema = new Tema();
        tema.setTitulo(datos.titulo());
        tema.setContenido(datos.contenido());
        tema.setCategoria(categoriaService.buscarPorId(datos.idCategoria()));
        tema.setIdUsuario(datos.idUsuario());
        return temaRepository.save(tema);
    }

    public Tema actualizar(Long id, TemaRequest datos) {
        Tema tema = buscarPorId(id);
        if (!tema.getIdUsuario().equals(datos.idUsuario())) {
            throw new AccesoDenegadoException("Solo el autor puede editar el tema " + id);
        }
        tema.setTitulo(datos.titulo());
        tema.setContenido(datos.contenido());
        tema.setCategoria(categoriaService.buscarPorId(datos.idCategoria()));
        return temaRepository.save(tema);
    }

    @Transactional
    public void eliminar(Long id) {
        Tema tema = buscarPorId(id);
        comentarioRepository.deleteByTema_Id(id);
        temaRepository.delete(tema);
    }

    public Tema cambiarEstado(Long id, EstadoContenido estado) {
        Tema tema = buscarPorId(id);
        tema.setEstado(estado);
        return temaRepository.save(tema);
    }
}
