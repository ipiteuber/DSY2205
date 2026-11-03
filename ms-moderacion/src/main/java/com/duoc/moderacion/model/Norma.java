package com.duoc.moderacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad Norma - tabla NORMAS en Oracle
@Entity
@Table(name = "NORMAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Norma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NORMA")
    private Long id;

    @NotBlank(message = "El título de la norma es obligatorio")
    @Size(max = 100, message = "No puede superar los 100 caracteres")
    @Column(name = "TITULO", nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "La descripción de la norma es obligatoria")
    @Size(max = 500, message = "No puede superar los 500 caracteres")
    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;
}
