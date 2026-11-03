package com.duoc.foro.model;

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

// Entidad Categoria - tabla CATEGORIAS en Oracle
@Entity
@Table(name = "CATEGORIAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIA")
    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "No puede superar los 50 caracteres")
    @Column(name = "NOMBRE", nullable = false, unique = true, length = 50)
    private String nombre;

    @Size(max = 200, message = "No puede superar los 200 caracteres")
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;
}
