package order.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import common.exception.DownstreamServiceException;
import common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;

@Component
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(
            @Value("${clients.customer-service.url}") String customerServiceUrl,
            @Value("${clients.connect-timeout-ms}") int connectTimeoutMs,
            @Value("${clients.read-timeout-ms}") int readTimeoutMs
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutMs);
        requestFactory.setReadTimeout(readTimeoutMs);

        this.restClient = RestClient.builder()
                .baseUrl(customerServiceUrl)
                .requestFactory(requestFactory)
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
        } catch (RestClientException exception) {
            throw new DownstreamServiceException("Customer service is unavailable.");
        }
    }

    public record CustomerResponse(
            Long id,
            String rut,
            @JsonAlias("nombre") String firstName,
            @JsonAlias("apellido") String lastName,
            @JsonAlias("correo") String email,
            @JsonAlias("telefono") String phone
    ) {
    }
}
