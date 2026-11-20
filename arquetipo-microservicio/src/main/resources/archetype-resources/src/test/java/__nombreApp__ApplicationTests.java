package ${package};

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de la API de ${recurso} con H2 en memoria
@SpringBootTest
@AutoConfigureMockMvc
class ${nombreApp}ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crudCompleto() throws Exception {
        mockMvc.perform(post("/api/${recurso}").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Prueba\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/${recurso}/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Prueba"));

        mockMvc.perform(put("/api/${recurso}/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Cambio\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cambio"));

        mockMvc.perform(delete("/api/${recurso}/1")).andExpect(status().isOk());
        mockMvc.perform(get("/api/${recurso}/1")).andExpect(status().isNotFound());
    }
}
