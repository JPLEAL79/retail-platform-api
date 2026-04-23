package order.client;

import common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(@Value("${clients.customer-service.url}") String customerServiceUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(customerServiceUrl)
                .build();
    }

    public CustomerResponse getById(Long customerId) {
        try {
            return restClient.get()
                    .uri("/customers/{id}", customerId)
                    .retrieve()
                    .body(CustomerResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("Customer not found with id " + customerId + ".");
        }
    }

    public record CustomerResponse(Long id, String rut, String firstName, String lastName, String email, String phone, Boolean active) {
    }
}
