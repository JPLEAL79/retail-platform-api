package com.jp.testplatformapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClienteRequest {

    @NotBlank(message = "El rut es obligatorio.")
    @Pattern(
            regexp = "^\\d{7,8}-[\\dkK]$",
            message = "El rut debe tener formato 12345678-9."
    )
    private String rut;

    @NotBlank(message = "El nombre es obligatorio.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe tener un formato valido.")
    private String correo;

    @NotBlank(message = "El telefono es obligatorio.")
    @Pattern(
            regexp = "^\\+?\\d{8,15}$",
            message = "El telefono debe contener solo numeros y puede incluir + al inicio."
    )
    private String telefono;

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
