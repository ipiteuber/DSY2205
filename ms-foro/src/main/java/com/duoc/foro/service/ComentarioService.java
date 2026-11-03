package com.duoc.foro.service;

import com.duoc.foro.client.UsuarioClient;
import com.duoc.foro.dto.ComentarioRequest;
import com.duoc.foro.exception.AccesoDenegadoException;
import com.duoc.foro.exception.RecursoNoEncontradoException;
import com.duoc.foro.exception.ReglaNegocioException;
import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.model.Tema;
import com.duoc.foro.repository.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Servicio de comentarios. Solo el autor puede editar y no se comenta un tema baneado.
@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TemaService temaService;
    private final UsuarioClient usuarioClient;

    public List<Comentario> listar() {
        return comentarioRepository.findAll();
    }

    public List<Comentario> listarBaneados() {
        return comentarioRepository.findByEstadoOrderByFechaDesc(EstadoContenido.BANEADO);
    }

    public Comentario buscarPorId(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un comentario con ID " + id));
    }

    public List<Comentario> listarPorTema(Long idTema) {
        temaService.buscarPorId(idTema);
        return comentarioRepository.findByTema_IdAndEstadoOrderByFechaAsc(idTema, EstadoContenido.ACTIVO);
    }

    public List<Comentario> buscarPorUsuario(Long idUsuario) {
        return comentarioRepository.findByIdUsuarioOrderByFechaDesc(idUsuario);
    }

    public Comentario comentar(Long idTema, ComentarioRequest datos) {
        Tema tema = temaService.buscarPorId(idTema);
        if (tema.getEstado() == EstadoContenido.BANEADO) {
            throw new ReglaNegocioException("No se puede comentar el tema " + idTema + " porque está baneado");
        }
        usuarioClient.validarUsuarioActivo(datos.idUsuario());
        Comentario comentario = new Comentario();
        comentario.setTema(tema);
        comentario.setIdUsuario(datos.idUsuario());
        comentario.setContenido(datos.contenido());
        return comentarioRepository.save(comentario);
    }

    public Comentario actualizar(Long id, ComentarioRequest datos) {
        Comentario comentario = buscarPorId(id);
        if (!comentario.getIdUsuario().equals(datos.idUsuario())) {
            throw new AccesoDenegadoException("Solo el autor puede editar el comentario " + id);
        }
        comentario.setContenido(datos.contenido());
        return comentarioRepository.save(comentario);
    }

    public void eliminar(Long id) {
        comentarioRepository.delete(buscarPorId(id));
    }

    public Comentario cambiarEstado(Long id, EstadoContenido estado) {
        Comentario comentario = buscarPorId(id);
        comentario.setEstado(estado);
        return comentarioRepository.save(comentario);
    }
}
