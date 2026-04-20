package com.jp.testplatformapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteRequest {

    @NotBlank(message = "El campo RUT es obligatorio.")
    @Size(max = 12, message = "El campo RUT no debe superar los 12 caracteres.")
    @Pattern(
            regexp = "^$|^\\d{7,8}-[\\dkK]$",
            message = "El campo RUT debe tener formato 12345678-9."
    )
    private String rut;

    @NotBlank(message = "El campo nombre es obligatorio.")
    @Size(max = 100, message = "El campo nombre no debe superar los 100 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo nombre debe contener texto válido.")
    private String nombre;

    @NotBlank(message = "El campo apellido es obligatorio.")
    @Size(max = 100, message = "El campo apellido no debe superar los 100 caracteres.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "El campo apellido debe contener texto válido.")
    private String apellido;

    @NotBlank(message = "El campo correo es obligatorio.")
    @Size(max = 150, message = "El campo correo no debe superar los 150 caracteres.")
    @Email(message = "El campo correo debe tener un formato válido.")
    private String correo;

    @NotBlank(message = "El campo teléfono es obligatorio.")
    @Size(max = 20, message = "El campo teléfono no debe superar los 20 caracteres.")
    @Pattern(
            regexp = "^$|^\\+?\\d{8,15}$",
            message = "El campo teléfono debe contener solo números y puede incluir + al inicio."
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
