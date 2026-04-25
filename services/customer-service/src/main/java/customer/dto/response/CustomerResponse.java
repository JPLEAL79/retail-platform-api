package customer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerResponse {

    private Long id;
    @JsonProperty("rut")
    private String rut;
    @JsonProperty("nombre")
    private String firstName;
    @JsonProperty("apellido")
    private String lastName;
    @JsonProperty("correo")
    private String email;
    @JsonProperty("telefono")
    private String phone;
    @JsonProperty("activo")
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
