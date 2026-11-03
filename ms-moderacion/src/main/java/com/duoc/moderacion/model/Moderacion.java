package com.duoc.moderacion.model;

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

// Entidad Moderacion - tabla MODERACIONES en Oracle
@Entity
@Table(name = "MODERACIONES")
@Data
@NoArgsConstructor
public class Moderacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MODERACION")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CONTENIDO", nullable = false, length = 15)
    private TipoContenido tipoContenido;

    // ID del tema o comentario en ms-foro
    @Column(name = "ID_CONTENIDO", nullable = false)
    private Long idContenido;

    // ID del usuario en ms-usuarios
    @Column(name = "ID_MODERADOR", nullable = false)
    private Long idModerador;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACCION", nullable = false, length = 15)
    private AccionModeracion accion;

    @ManyToOne
    @JoinColumn(name = "ID_NORMA")
    private Norma norma;

    @Column(name = "MOTIVO", nullable = false, length = 500)
    private String motivo;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    void alCrear() {
        if (fecha == null) fecha = LocalDateTime.now();
    }
}
