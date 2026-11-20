package com.duoc.foro.service;

import com.duoc.foro.client.UsuarioClient;
import com.duoc.foro.dto.ComentarioDTO;
import com.duoc.foro.dto.ComentarioRequest;
import com.duoc.foro.exception.AccesoDenegadoException;
import com.duoc.foro.exception.RecursoNoEncontradoException;
import com.duoc.foro.exception.ReglaNegocioException;
import com.duoc.foro.mapper.ComentarioMapper;
import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.model.Tema;
import com.duoc.foro.repository.ComentarioRepository;
import com.duoc.foro.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementacion del servicio de comentarios. Solo el autor puede editar y no se comenta un tema baneado.
@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TemaRepository temaRepository;
    private final ComentarioMapper comentarioMapper;
    private final UsuarioClient usuarioClient;

    @Override
    public List<ComentarioDTO> listar() {
        return aDtos(comentarioRepository.findAll());
    }

    @Override
    public List<ComentarioDTO> listarBaneados() {
        return aDtos(comentarioRepository.findByEstadoOrderByFechaDesc(EstadoContenido.BANEADO));
    }

    @Override
    public ComentarioDTO buscarPorId(Long id) {
        return comentarioMapper.aDto(obtener(id));
    }

    @Override
    public List<ComentarioDTO> listarPorTema(Long idTema) {
        obtenerTema(idTema);
        return aDtos(comentarioRepository.findByTema_IdAndEstadoOrderByFechaAsc(idTema, EstadoContenido.ACTIVO));
    }

    @Override
    public List<ComentarioDTO> buscarPorUsuario(Long idUsuario) {
        return aDtos(comentarioRepository.findByIdUsuarioOrderByFechaDesc(idUsuario));
    }

    @Override
    public ComentarioDTO comentar(Long idTema, ComentarioRequest datos) {
        Tema tema = obtenerTema(idTema);
        if (tema.getEstado() == EstadoContenido.BANEADO) {
            throw new ReglaNegocioException("No se puede comentar el tema " + idTema + " porque está baneado");
        }
        usuarioClient.validarUsuarioActivo(datos.idUsuario());
        return comentarioMapper.aDto(comentarioRepository.save(comentarioMapper.aEntidad(datos, tema)));
    }

    @Override
    public ComentarioDTO actualizar(Long id, ComentarioRequest datos) {
        Comentario comentario = obtener(id);
        if (!comentario.getIdUsuario().equals(datos.idUsuario())) {
            throw new AccesoDenegadoException("Solo el autor puede editar el comentario " + id);
        }
        comentario.setContenido(datos.contenido());
        return comentarioMapper.aDto(comentarioRepository.save(comentario));
    }

    @Override
    public void eliminar(Long id) {
        comentarioRepository.delete(obtener(id));
    }

    @Override
    public ComentarioDTO cambiarEstado(Long id, EstadoContenido estado) {
        Comentario comentario = obtener(id);
        comentario.setEstado(estado);
        return comentarioMapper.aDto(comentarioRepository.save(comentario));
    }

    private Comentario obtener(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un comentario con ID " + id));
    }

    private Tema obtenerTema(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con ID " + id));
    }

    private List<ComentarioDTO> aDtos(List<Comentario> comentarios) {
        return comentarios.stream().map(comentarioMapper::aDto).toList();
    }
}
