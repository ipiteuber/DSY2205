package com.duoc.moderacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Entidad Norma - tabla NORMAS en Oracle
@Entity
@Table(name = "NORMAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Norma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NORMA")
    private Long id;

    @Column(name = "TITULO", nullable = false, length = 100)
    private String titulo;

    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;
}
