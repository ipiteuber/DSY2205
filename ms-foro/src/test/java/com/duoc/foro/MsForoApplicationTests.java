package com.duoc.foro;

import com.duoc.foro.client.UsuarioClient;
import com.duoc.foro.dto.UsuarioRemoto;
import com.duoc.foro.exception.ReglaNegocioException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de la API del foro con H2 en memoria. ms-usuarios se simula con Mockito.
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MsForoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioClient usuarioClient;

    @BeforeEach
    void crearDatos() throws Exception {
        when(usuarioClient.validarUsuarioActivo(anyLong())).thenReturn(new UsuarioRemoto(3L, "juanito_dev", true));
        enviar("/api/categorias", """
                {"nombre":"Tecnología","descripcion":"Programación y gadgets"}""");
        enviar("/api/categorias", """
                {"nombre":"Videojuegos","descripcion":"Juegos de todas las plataformas"}""");
        enviar("/api/temas", """
                {"titulo":"¿Qué lenguaje aprender primero?","contenido":"Estoy entre Java y Python",
                 "idCategoria":1,"idUsuario":3}""");
    }

    @Test
    void verDetalleSumaVisitasYTraeComentarios() throws Exception {
        enviar("/api/temas/1/comentarios", """
                {"contenido":"Java, por Spring Boot","idUsuario":3}""");
        mockMvc.perform(get("/api/temas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tema.visitas").value(1))
                .andExpect(jsonPath("$.tema.categoria.nombre").value("Tecnología"))
                .andExpect(jsonPath("$.totalComentarios").value(1))
                .andExpect(jsonPath("$.comentarios[0].idTema").value(1));
    }

    @Test
    void buscaPorTextoYCategoria() throws Exception {
        mockMvc.perform(get("/api/temas/buscar").param("texto", "python"))
                .andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/api/temas/categoria/2")).andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get("/api/categorias/resumen"))
                .andExpect(jsonPath("$[0].categoria").value("Tecnología"))
                .andExpect(jsonPath("$[0].temasActivos").value(1));
    }

    @Test
    void temaBaneadoNoSeMuestraNiSeComenta() throws Exception {
        mockMvc.perform(patch("/api/temas/1/estado").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"estado":"BANEADO"}"""))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/temas/1")).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/temas")).andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(post("/api/temas/1/comentarios").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"contenido":"Hola","idUsuario":3}"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void soloElAutorEditaElTema() throws Exception {
        mockMvc.perform(put("/api/temas/1").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Título cambiado","contenido":"Otro","idCategoria":1,"idUsuario":4}"""))
                .andExpect(status().isForbidden());
    }

    @Test
    void noPublicaSiElUsuarioNoExiste() throws Exception {
        when(usuarioClient.validarUsuarioActivo(99L)).thenThrow(new ReglaNegocioException("El usuario con ID 99 no existe"));
        mockMvc.perform(post("/api/temas").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"titulo":"Tema fantasma","contenido":"x","idCategoria":1,"idUsuario":99}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El usuario con ID 99 no existe"));
    }

    @Test
    void noEliminaCategoriaConTemas() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isConflict());
    }

    private void enviar(String url, String json) throws Exception {
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }
}
