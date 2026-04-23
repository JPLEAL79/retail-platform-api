package customer.service;

import customer.entity.Customer;
import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Customer getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id + "."));
    }

    public Customer getByRut(String rut) {
        return repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with RUT " + rut + "."));
    }

    public Customer create(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        ensureUniqueBusinessFields(customer, null);
        return repository.save(customer);
    }

    public Customer update(Long id, Customer updatedCustomer) {
        if (updatedCustomer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        Customer existingCustomer = getById(id);
        ensureUniqueBusinessFields(updatedCustomer, id);

        existingCustomer.setRut(updatedCustomer.getRut());
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setActive(updatedCustomer.getActive());

        return repository.save(existingCustomer);
    }

    public void delete(Long id) {
        Customer customer = getById(id);

        // We keep the customer row because orders may reference this id in another service.
        customer.setActive(false);
        repository.save(customer);
    }

    private void ensureUniqueBusinessFields(Customer customer, Long currentCustomerId) {
        if (currentCustomerId == null) {
            if (repository.existsByRut(customer.getRut())) {
                throw new ConflictException("A customer with this RUT already exists.");
            }

            if (repository.existsByEmail(customer.getEmail())) {
                throw new ConflictException("A customer with this email already exists.");
            }

            if (repository.existsByPhone(customer.getPhone())) {
                throw new ConflictException("A customer with this phone already exists.");
            }

            return;
        }

        if (repository.existsByRutAndIdNot(customer.getRut(), currentCustomerId)) {
            throw new ConflictException("A customer with this RUT already exists.");
        }

        if (repository.existsByEmailAndIdNot(customer.getEmail(), currentCustomerId)) {
            throw new ConflictException("A customer with this email already exists.");
        }

        if (repository.existsByPhoneAndIdNot(customer.getPhone(), currentCustomerId)) {
            throw new ConflictException("A customer with this phone already exists.");
        }
    }
}
