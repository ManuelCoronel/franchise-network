package co.com.bancolombia.api.product;

import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.api.product.ProductHandler;
import co.com.bancolombia.api.product.RouterRest;
import co.com.bancolombia.api.dto.product.request.ProductCreateRequestDto;
import co.com.bancolombia.api.dto.product.request.ProductUpdateNameRequestDto;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RouterRestTest {

    private WebTestClient webTestClient;
    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        productUseCase = mock(ProductUseCase.class);
        ProductHandler handler = new ProductHandler(productUseCase, new ObjectMapper(), new ValidationHelper(validator));

        RouterRest routerRest = new RouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerProductFunction(handler)).build();
    }

    @Test
    void createProduct_ShouldReturnCreated() {
        // Arrange
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setName("Test Product");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        when(productUseCase.saveProduct(any(Product.class)))
                .thenReturn(Mono.just(product));

        // Act & Assert
        webTestClient.post()
                .uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("Test Product");

        verify(productUseCase, times(1)).saveProduct(any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnCreated() {
        // Arrange
        ProductUpdateNameRequestDto requestDto = new ProductUpdateNameRequestDto();
        requestDto.setProductId(1L);
        requestDto.setName("Updated Product");

        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        when(productUseCase.updateProductName(1L, "Updated Product"))
                .thenReturn(Mono.just(product));

        // Act & Assert
        webTestClient.put()
                .uri("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("Updated Product");

        verify(productUseCase, times(1)).updateProductName(1L, "Updated Product");
    }
}
