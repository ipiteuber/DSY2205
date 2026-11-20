package com.duoc.foro.service;

import com.duoc.foro.client.UsuarioClient;
import com.duoc.foro.dto.ComentarioDTO;
import com.duoc.foro.dto.TemaDTO;
import com.duoc.foro.dto.TemaDetalle;
import com.duoc.foro.dto.TemaRequest;
import com.duoc.foro.exception.AccesoDenegadoException;
import com.duoc.foro.exception.RecursoNoEncontradoException;
import com.duoc.foro.mapper.ComentarioMapper;
import com.duoc.foro.mapper.TemaMapper;
import com.duoc.foro.model.Categoria;
import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.model.Tema;
import com.duoc.foro.repository.CategoriaRepository;
import com.duoc.foro.repository.ComentarioRepository;
import com.duoc.foro.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implementacion del servicio de temas, maneja publicar, buscar, editar y el estado ACTIVO / BANEADO
@Service
@RequiredArgsConstructor
public class TemaServiceImpl implements TemaService {

    private final TemaRepository temaRepository;
    private final ComentarioRepository comentarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final TemaMapper temaMapper;
    private final ComentarioMapper comentarioMapper;
    private final UsuarioClient usuarioClient;

    @Override
    public List<TemaDTO> listarActivos() {
        return aDtos(temaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoContenido.ACTIVO));
    }

    @Override
    public List<TemaDTO> listarBaneados() {
        return aDtos(temaRepository.findByEstadoOrderByFechaCreacionDesc(EstadoContenido.BANEADO));
    }

    @Override
    @Transactional
    public TemaDetalle verDetalle(Long id) {
        Tema tema = obtener(id);
        if (tema.getEstado() == EstadoContenido.BANEADO) {
            throw new AccesoDenegadoException("El tema " + id + " fue baneado por incumplir las normas del foro");
        }
        tema.setVisitas(tema.getVisitas() + 1);
        List<ComentarioDTO> comentarios = comentarioRepository
                .findByTema_IdAndEstadoOrderByFechaAsc(id, EstadoContenido.ACTIVO).stream()
                .map(comentarioMapper::aDto)
                .toList();
        return new TemaDetalle(temaMapper.aDto(tema), comentarios.size(), comentarios);
    }

    @Override
    public List<TemaDTO> buscarPorCategoria(Long idCategoria) {
        obtenerCategoria(idCategoria);
        return aDtos(temaRepository.findByCategoria_IdAndEstadoOrderByFechaCreacionDesc(idCategoria, EstadoContenido.ACTIVO));
    }

    @Override
    public List<TemaDTO> buscarPorTexto(String texto) {
        return aDtos(temaRepository.buscarPorTexto(texto, EstadoContenido.ACTIVO));
    }

    @Override
    public List<TemaDTO> buscarPorUsuario(Long idUsuario) {
        return aDtos(temaRepository.findByIdUsuarioOrderByFechaCreacionDesc(idUsuario));
    }

    @Override
    public List<TemaDTO> populares() {
        return aDtos(temaRepository.findTop5ByEstadoOrderByVisitasDesc(EstadoContenido.ACTIVO));
    }

    @Override
    public TemaDTO publicar(TemaRequest datos) {
        usuarioClient.validarUsuarioActivo(datos.idUsuario());
        Tema tema = temaMapper.aEntidad(datos, obtenerCategoria(datos.idCategoria()));
        return temaMapper.aDto(temaRepository.save(tema));
    }

    @Override
    public TemaDTO actualizar(Long id, TemaRequest datos) {
        Tema tema = obtener(id);
        if (!tema.getIdUsuario().equals(datos.idUsuario())) {
            throw new AccesoDenegadoException("Solo el autor puede editar el tema " + id);
        }
        temaMapper.actualizar(tema, datos, obtenerCategoria(datos.idCategoria()));
        return temaMapper.aDto(temaRepository.save(tema));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Tema tema = obtener(id);
        comentarioRepository.deleteByTema_Id(id);
        temaRepository.delete(tema);
    }

    @Override
    public TemaDTO cambiarEstado(Long id, EstadoContenido estado) {
        Tema tema = obtener(id);
        tema.setEstado(estado);
        return temaMapper.aDto(temaRepository.save(tema));
    }

    private Tema obtener(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con ID " + id));
    }

    private Categoria obtenerCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una categoría con ID " + id));
    }

    private List<TemaDTO> aDtos(List<Tema> temas) {
        return temas.stream().map(temaMapper::aDto).toList();
    }
}
