package customer.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @NotBlank(message = "RUT is required.")
    @Size(max = 12, message = "RUT must not exceed 12 characters.")
    @Pattern(
            regexp = "^$|^\\d{7,8}-[\\dkK]$",
            message = "RUT must use format 12345678-9."
    )
    @JsonProperty("rut")
    private String rut;

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "First name must contain valid text.")
    @JsonProperty("nombre")
    @JsonAlias("firstName")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Last name must contain valid text.")
    @JsonProperty("apellido")
    @JsonAlias("lastName")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Size(max = 150, message = "Email must not exceed 150 characters.")
    @Email(message = "Email must use a valid format.")
    @JsonProperty("correo")
    @JsonAlias("email")
    private String email;

    @NotBlank(message = "Phone is required.")
    @Size(max = 20, message = "Phone must not exceed 20 characters.")
    @Pattern(
            regexp = "^$|^\\+?\\d{8,15}$",
            message = "Phone must contain only digits and may start with +."
    )
    @JsonProperty("telefono")
    @JsonAlias("phone")
    private String phone;

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

}
