package com.jp.testplatformapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "direcciones_entrega")
public class DireccionEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false, length = 100)
    private String comuna;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(name = "numero_depto", length = 50)
    private String numeroDepto;

    @Column(length = 200)
    private String referencia;

    // One order can carry one delivery address snapshot.
    @OneToOne
    @JoinColumn(name = "orden_id", nullable = false, unique = true)
    @JsonIgnore
    private Orden orden;

    public DireccionEntrega() {
    }
}
