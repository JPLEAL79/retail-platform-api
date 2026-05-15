package customer.service;

import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import customer.entity.Customer;
import customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CustomerRepository repository = mock(CustomerRepository.class);
    private final CustomerService service = new CustomerService(repository);

    // Customer business keys must stay unique because they identify real contact data.
    @Test
    void createRejectsDuplicatedRut() {
        Customer customer = customer();
        when(repository.existsByRut(customer.getRut())).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(customer));

        verify(repository).existsByRut(customer.getRut());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createRejectsDuplicatedEmail() {
        Customer customer = customer();
        when(repository.existsByEmail(customer.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.create(customer));

        verify(repository).existsByRut(customer.getRut());
        verify(repository).existsByEmail(customer.getEmail());
        verifyNoMoreInteractions(repository);
    }

    // Missing resources should fail with the same domain exception used by the API layer.
    @Test
    void getByIdRejectsMissingCustomer() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    private Customer customer() {
        Customer customer = new Customer();
        customer.setRut("12345678-9");
        customer.setFirstName("Juan");
        customer.setLastName("Perez");
        customer.setEmail("juan.perez@test.com");
        customer.setPhone("+56912345678");
        return customer;
    }
}
