package com.duoc.foro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Entidad Comentario - tabla COMENTARIOS en Oracle
@Entity
@Table(name = "COMENTARIOS")
@Data
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COMENTARIO")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_TEMA", nullable = false)
    private Tema tema;

    @Column(name = "ID_USUARIO", nullable = false)
    private Long idUsuario;

    @Column(name = "CONTENIDO", nullable = false, length = 1000)
    private String contenido;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 10)
    private EstadoContenido estado;

    // Se muestra solo el id del tema para no repetir el tema completo en cada comentario
    @JsonProperty("idTema")
    public Long getIdTema() {
        return tema != null ? tema.getId() : null;
    }

    @PrePersist
    void alCrear() {
        if (fecha == null) fecha = LocalDateTime.now();
        if (estado == null) estado = EstadoContenido.ACTIVO;
    }
}
