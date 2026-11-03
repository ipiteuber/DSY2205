package com.duoc.usuarios.model;

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

// Entidad Rol - tabla ROLES en Oracle
@Entity
@Table(name = "ROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 30, message = "No puede superar los 30 caracteres")
    @Column(name = "NOMBRE", nullable = false, unique = true, length = 30)
    private String nombre;

    @Size(max = 200, message = "No puede superar los 200 caracteres")
    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;
}
