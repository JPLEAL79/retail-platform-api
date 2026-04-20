package com.jp.testplatformapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DireccionEntregaRequest {

    @NotBlank(message = "El campo región es obligatorio.")
    @Size(max = 100, message = "El campo región no debe superar los 100 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo región debe contener texto válido.")
    private String region;

    @NotBlank(message = "El campo comuna es obligatorio.")
    @Size(max = 100, message = "El campo comuna no debe superar los 100 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo comuna debe contener texto válido.")
    private String comuna;

    @NotBlank(message = "El campo dirección es obligatorio.")
    @Size(max = 150, message = "El campo dirección no debe superar los 150 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo dirección debe contener texto válido.")
    private String direccion;

    @NotBlank(message = "El campo número es obligatorio.")
    @Size(max = 20, message = "El campo número no debe superar los 20 caracteres.")
    @Pattern(regexp = "^$|^[0-9]+$", message = "El campo número debe contener solo dígitos.")
    private String numero;

    @Size(max = 50, message = "El campo numeroDepto no debe superar los 50 caracteres.")
    private String numeroDepto;

    @Size(max = 200, message = "El campo referencia no debe superar los 200 caracteres.")
    private String referencia;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroDepto() {
        return numeroDepto;
    }

    public void setNumeroDepto(String numeroDepto) {
        this.numeroDepto = numeroDepto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
