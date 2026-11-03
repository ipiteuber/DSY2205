package com.duoc.moderacion.service;

import com.duoc.moderacion.client.ForoClient;
import com.duoc.moderacion.client.UsuarioClient;
import com.duoc.moderacion.dto.ModeracionRequest;
import com.duoc.moderacion.dto.ModeracionesPorNorma;
import com.duoc.moderacion.dto.UsuarioRemoto;
import com.duoc.moderacion.exception.AccesoDenegadoException;
import com.duoc.moderacion.exception.RecursoNoEncontradoException;
import com.duoc.moderacion.exception.ReglaNegocioException;
import com.duoc.moderacion.model.AccionModeracion;
import com.duoc.moderacion.model.Moderacion;
import com.duoc.moderacion.model.TipoContenido;
import com.duoc.moderacion.repository.ModeracionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

// Servicio de moderacion, maneja el flujo: Banear -> restaurar. Revisa el rol en ms-usuarios.
@Service
@RequiredArgsConstructor
public class ModeracionService {

    // ADMIN y MODERADOR pueden banear, pero solo ADMIN puede restaurar
    private static final Set<String> ROLES_BANEAR = Set.of("ADMIN", "MODERADOR");
    private static final Set<String> ROLES_RESTAURAR = Set.of("ADMIN");

    private final ModeracionRepository moderacionRepository;
    private final NormaService normaService;
    private final UsuarioClient usuarioClient;
    private final ForoClient foroClient;

    public List<Moderacion> listar() {
        return moderacionRepository.findAllByOrderByFechaDesc();
    }

    public Moderacion buscarPorId(Long id) {
        return moderacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una moderación con ID " + id));
    }

    public List<Moderacion> buscarPorModerador(Long idModerador) {
        return moderacionRepository.findByIdModeradorOrderByFechaDesc(idModerador);
    }

    public List<Moderacion> buscarPorTipo(TipoContenido tipo) {
        return moderacionRepository.findByTipoContenidoOrderByFechaDesc(tipo);
    }

    public List<Moderacion> historialDeContenido(TipoContenido tipo, Long idContenido) {
        return moderacionRepository.findByTipoContenidoAndIdContenidoOrderByFechaAsc(tipo, idContenido);
    }

    public List<ModeracionesPorNorma> resumenPorNorma() {
        return moderacionRepository.contarPorNorma(AccionModeracion.BANEAR);
    }

    public Moderacion banear(TipoContenido tipo, Long idContenido, ModeracionRequest datos) {
        validarRol(datos.idModerador(), ROLES_BANEAR, "banear contenido");
        if (datos.idNorma() == null) {
            throw new ReglaNegocioException("Para banear hay que indicar la norma que se incumplió (idNorma)");
        }
        Moderacion moderacion = nuevaModeracion(tipo, idContenido, AccionModeracion.BANEAR, datos);
        foroClient.cambiarEstado(tipo, idContenido, "BANEADO");
        return moderacionRepository.save(moderacion);
    }

    public Moderacion restaurar(TipoContenido tipo, Long idContenido, ModeracionRequest datos) {
        validarRol(datos.idModerador(), ROLES_RESTAURAR, "restaurar contenido");
        Moderacion moderacion = nuevaModeracion(tipo, idContenido, AccionModeracion.RESTAURAR, datos);
        foroClient.cambiarEstado(tipo, idContenido, "ACTIVO");
        return moderacionRepository.save(moderacion);
    }

    public Moderacion actualizar(Long id, ModeracionRequest datos) {
        Moderacion moderacion = buscarPorId(id);
        moderacion.setMotivo(datos.motivo());
        if (datos.idNorma() != null) {
            moderacion.setNorma(normaService.buscarPorId(datos.idNorma()));
        }
        return moderacionRepository.save(moderacion);
    }

    public void eliminar(Long id) {
        moderacionRepository.delete(buscarPorId(id));
    }

    private void validarRol(Long idUsuario, Set<String> rolesPermitidos, String accion) {
        UsuarioRemoto usuario = usuarioClient.obtenerUsuario(idUsuario);
        if (!Boolean.TRUE.equals(usuario.activo())) {
            throw new AccesoDenegadoException("El usuario " + usuario.nombreUsuario() + " está desactivado");
        }
        if (!rolesPermitidos.contains(usuario.nombreRol())) {
            throw new AccesoDenegadoException("El usuario " + usuario.nombreUsuario() + " tiene rol "
                    + usuario.nombreRol() + " y no tiene permiso para " + accion);
        }
    }

    private Moderacion nuevaModeracion(TipoContenido tipo, Long idContenido, AccionModeracion accion, ModeracionRequest datos) {
        Moderacion moderacion = new Moderacion();
        moderacion.setTipoContenido(tipo);
        moderacion.setIdContenido(idContenido);
        moderacion.setIdModerador(datos.idModerador());
        moderacion.setAccion(accion);
        moderacion.setMotivo(datos.motivo());
        if (datos.idNorma() != null) {
            moderacion.setNorma(normaService.buscarPorId(datos.idNorma()));
        }
        return moderacion;
    }
}
