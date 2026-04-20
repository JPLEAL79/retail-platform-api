package com.jp.testplatformapi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductoRequest {

    @NotBlank(message = "El campo SKU es obligatorio.")
    @Size(max = 30, message = "El campo SKU no debe superar los 30 caracteres.")
    @Pattern(regexp = "^$|^[A-Za-z0-9-]+$", message = "El campo SKU solo debe contener letras, números y guion.")
    private String sku;

    @NotBlank(message = "El campo nombre es obligatorio.")
    @Size(max = 150, message = "El campo nombre no debe superar los 150 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo nombre debe contener texto válido.")
    private String nombre;

    @NotBlank(message = "El campo marca es obligatorio.")
    @Size(max = 100, message = "El campo marca no debe superar los 100 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo marca debe contener texto válido.")
    private String marca;

    @NotNull(message = "El campo precio es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El campo precio debe ser mayor que cero.")
    @Digits(integer = 12, fraction = 0, message = "El campo precio en CLP no debe tener decimales.")
    private BigDecimal precio;

    @NotNull(message = "El campo stock es obligatorio.")
    @PositiveOrZero(message = "El campo stock no puede ser negativo.")
    private Integer stock;

    @NotNull(message = "El campo activo es obligatorio.")
    private Boolean activo;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
