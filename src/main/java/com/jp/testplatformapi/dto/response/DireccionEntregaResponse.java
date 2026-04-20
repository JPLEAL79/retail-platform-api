package com.jp.testplatformapi.dto.response;

public class DireccionEntregaResponse {

    private String region;
    private String comuna;
    private String direccion;
    private String numero;
    private String numeroDepto;
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
