package co.com.bancolombia.api.product;

import co.com.bancolombia.api.dto.product.request.ProductCreateRequestDto;
import co.com.bancolombia.api.dto.product.request.ProductUpdateNameRequestDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductHandlerTest {

    @Mock
    private ProductUseCase productUseCase;

    private ValidationHelper validationHelper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private ProductHandler productHandler;

    private ProductCreateRequestDto createRequestDto;
    private ProductUpdateNameRequestDto updateNameRequestDto;
    private Product product;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.validationHelper = new ValidationHelper(validator);

        createRequestDto = new ProductCreateRequestDto();
        createRequestDto.setName("Laptop");


        updateNameRequestDto = new ProductUpdateNameRequestDto();
        updateNameRequestDto.setProductId(1L);
        updateNameRequestDto.setName("Smartphone");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");

        objectMapper = new ObjectMapper();
        productHandler = new ProductHandler(productUseCase, objectMapper, validationHelper);

    }

    @Test
    void createProduct_success() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(ProductCreateRequestDto.class)).thenReturn(Mono.just(createRequestDto));
        when(productUseCase.saveProduct(any(Product.class))).thenReturn(Mono.just(product));

        // When
        Mono<ServerResponse> response = productHandler.createProduct(request);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();

        verify(productUseCase, times(1)).saveProduct(any(Product.class));
    }


    @Test
    void updateNameProduct_success() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(ProductUpdateNameRequestDto.class)).thenReturn(Mono.just(updateNameRequestDto));

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Smartphone");


        when(productUseCase.updateProductName(1L, "Smartphone")).thenReturn(Mono.just(updatedProduct));

        // When
        Mono<ServerResponse> response = productHandler.updateNameProduct(request);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();

        verify(productUseCase, times(1)).updateProductName(1L, "Smartphone");
    }

}
