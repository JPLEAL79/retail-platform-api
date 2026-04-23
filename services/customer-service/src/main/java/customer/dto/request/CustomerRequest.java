package customer.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @NotBlank(message = "RUT is required.")
    @Size(max = 12, message = "RUT must not exceed 12 characters.")
    @Pattern(
            regexp = "^$|^\\d{7,8}-[\\dkK]$",
            message = "RUT must use format 12345678-9."
    )
    private String rut;

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "First name must contain valid text.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    @Pattern(regexp = "^$|.*\\p{L}.*", message = "Last name must contain valid text.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Size(max = 150, message = "Email must not exceed 150 characters.")
    @Email(message = "Email must use a valid format.")
    private String email;

    @NotBlank(message = "Phone is required.")
    @Size(max = 20, message = "Phone must not exceed 20 characters.")
    @Pattern(
            regexp = "^$|^\\+?\\d{8,15}$",
            message = "Phone must contain only digits and may start with +."
    )
    private String phone;

    @NotNull(message = "Active flag is required.")
    private Boolean active;

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
