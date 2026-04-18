package com.jp.testplatformapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String marca;

    // CLP is usually stored without decimal places.
    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    // A product can stay in the catalog but be unavailable for sale.
    @Column(nullable = false)
    private Boolean activo;

    public Producto() {
    }
}
