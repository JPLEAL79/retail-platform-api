package customer.service;

import customer.entity.Customer;
import common.exception.ConflictException;
import common.exception.ResourceNotFoundException;
import customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Page<Customer> getAll(Boolean active, int page, int size) {
        Pageable pageable = buildPageable(page, size);

        if (active != null) {
            return repository.findByActiveOrderByIdAsc(active, pageable);
        }

        return repository.findAllByOrderByIdAsc(pageable);
    }

    public Customer getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id + "."));
    }

    public Customer getByRut(String rut) {
        return repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with RUT " + rut + "."));
    }

    @Transactional
    public Customer create(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        ensureUniqueBusinessFields(customer, null);
        return repository.save(customer);
    }

    @Transactional
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

    @Transactional
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

    private Pageable buildPageable(int page, int size) {
        int resolvedPage = Math.max(page, 0);
        int resolvedSize = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(resolvedPage, resolvedSize);
    }
}
