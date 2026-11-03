package com.duoc.usuarios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de la API de usuarios y del login con H2 en memoria
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MsUsuariosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void crearDatos() throws Exception {
        enviar("/api/roles", """
                {"nombre":"ADMIN","descripcion":"Administra el foro"}""");
        enviar("/api/roles", """
                {"nombre":"USUARIO","descripcion":"Publica y comenta"}""");
        enviar("/api/usuarios", """
                {"nombreUsuario":"admin","email":"admin@foro.cl","password":"Admin2026",
                 "nombreCompleto":"Administrador del Foro","idRol":1}""");
    }

    @Test
    void loginCorrectoDevuelveElRol() throws Exception {
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":"admin","password":"Admin2026"}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void loginConPasswordIncorrectaDevuelve401() throws Exception {
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"usuario":"admin@foro.cl","password":"otra"}"""))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void laPasswordNoSeExponeEnElJson() throws Exception {
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.rol.nombre").value("ADMIN"));
    }

    @Test
    void noPermiteNombreDeUsuarioRepetido() throws Exception {
        mockMvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombreUsuario":"ADMIN","email":"otro@foro.cl","password":"Clave2026",
                                 "nombreCompleto":"Copia","idRol":2}"""))
                .andExpect(status().isConflict());
    }

    @Test
    void noEliminaRolConUsuarios() throws Exception {
        mockMvc.perform(delete("/api/roles/1")).andExpect(status().isConflict());
        mockMvc.perform(delete("/api/roles/2")).andExpect(status().isOk());
    }

    @Test
    void filtraPorRolYResume() throws Exception {
        mockMvc.perform(get("/api/usuarios/rol/admin")).andExpect(jsonPath("$", hasSize(1)));
        mockMvc.perform(get("/api/usuarios/resumen-roles"))
                .andExpect(jsonPath("$[0].rol").value("ADMIN"))
                .andExpect(jsonPath("$[0].cantidad").value(1))
                .andExpect(jsonPath("$[1].cantidad").value(0));
    }

    private void enviar(String url, String json) throws Exception {
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }
}
