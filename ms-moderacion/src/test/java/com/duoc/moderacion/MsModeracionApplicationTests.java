package com.duoc.moderacion;

import com.duoc.moderacion.client.ForoClient;
import com.duoc.moderacion.client.UsuarioClient;
import com.duoc.moderacion.dto.UsuarioRemoto;
import com.duoc.moderacion.model.TipoContenido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de la API de moderacion con H2 en memoria. ms-usuarios y ms-foro se simulan con Mockito.
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MsModeracionApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioClient usuarioClient;

    @MockitoBean
    private ForoClient foroClient;

    @BeforeEach
    void crearDatos() throws Exception {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(usuario(1L, "admin", "ADMIN"));
        when(usuarioClient.obtenerUsuario(2L)).thenReturn(usuario(2L, "mod_camila", "MODERADOR"));
        when(usuarioClient.obtenerUsuario(3L)).thenReturn(usuario(3L, "juanito_dev", "USUARIO"));
        mockMvc.perform(post("/api/normas").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Prohibido el spam","descripcion":"No se permite publicidad"}"""))
                .andExpect(status().isCreated());
    }

    @Test
    void moderadorPuedeBanearTema() throws Exception {
        mockMvc.perform(post("/api/moderacion/temas/5/banear").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idModerador":2,"idNorma":1,"motivo":"Venta de cuentas"}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accion").value("BANEAR"))
                .andExpect(jsonPath("$.norma.titulo").value("Prohibido el spam"));
        verify(foroClient).cambiarEstado(TipoContenido.TEMA, 5L, "BANEADO");
        mockMvc.perform(get("/api/moderacion/resumen-normas")).andExpect(jsonPath("$[0].baneos").value(1));
    }

    @Test
    void usuarioComunNoPuedeBanear() throws Exception {
        mockMvc.perform(post("/api/moderacion/comentarios/4/banear").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idModerador":3,"idNorma":1,"motivo":"No me gusta"}"""))
                .andExpect(status().isForbidden());
        verify(foroClient, never()).cambiarEstado(any(), any(), any());
    }

    @Test
    void soloAdminPuedeRestaurar() throws Exception {
        String cuerpo = """
                {"idModerador":%d,"motivo":"Apelación aceptada"}""";
        mockMvc.perform(post("/api/moderacion/temas/5/restaurar").contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo.formatted(2)))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/api/moderacion/temas/5/restaurar").contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo.formatted(1)))
                .andExpect(status().isCreated());
        verify(foroClient).cambiarEstado(TipoContenido.TEMA, 5L, "ACTIVO");
        mockMvc.perform(get("/api/moderacion/historial/TEMA/5")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void banearSinNormaDevuelve400() throws Exception {
        mockMvc.perform(post("/api/moderacion/temas/5/banear").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idModerador":1,"motivo":"Spam"}"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noEliminaNormaUsada() throws Exception {
        mockMvc.perform(post("/api/moderacion/temas/5/banear").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"idModerador":1,"idNorma":1,"motivo":"Spam"}"""));
        mockMvc.perform(delete("/api/normas/1")).andExpect(status().isConflict());
    }

    private UsuarioRemoto usuario(Long id, String nombre, String rol) {
        return new UsuarioRemoto(id, nombre, true, new UsuarioRemoto.RolRemoto(1L, rol));
    }
}
