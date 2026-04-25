package order.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import common.exception.ConflictException;
import common.exception.DownstreamServiceException;
import common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(
            @Value("${clients.product-service.url}") String productServiceUrl,
            @Value("${clients.connect-timeout-ms}") int connectTimeoutMs,
            @Value("${clients.read-timeout-ms}") int readTimeoutMs
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeoutMs);
        requestFactory.setReadTimeout(readTimeoutMs);

        this.restClient = RestClient.builder()
                .baseUrl(productServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public ProductResponse getById(Long productId) {
        try {
            return restClient.get()
                    .uri("/products/{id}", productId)
                    .retrieve()
                    .body(ProductResponse.class);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("Product not found with id " + productId + ".");
        } catch (RestClientException exception) {
            throw new DownstreamServiceException("Product service is unavailable.");
        }
    }

    public void adjustStock(Long productId, int delta) {
        try {
            restClient.post()
                    .uri("/products/{id}/stock-adjustments", productId)
                    .body(new StockAdjustmentRequest(delta))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new ResourceNotFoundException("Product not found with id " + productId + ".");
        } catch (HttpClientErrorException.Conflict exception) {
            throw new ConflictException("Product " + productId + " does not have enough stock.");
        } catch (RestClientException exception) {
            throw new DownstreamServiceException("Product service is unavailable.");
        }
    }

    public record ProductResponse(
            Long id,
            String sku,
            @JsonAlias("nombre") String name,
            @JsonAlias("marca") String brand,
            @JsonAlias("precio") BigDecimal price,
            Integer stock
    ) {
    }

    private record StockAdjustmentRequest(int delta) {
    }
}
