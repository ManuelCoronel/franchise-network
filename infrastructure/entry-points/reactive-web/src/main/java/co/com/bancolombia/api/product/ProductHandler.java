package co.com.bancolombia.api.product;

import co.com.bancolombia.api.dto.ApiResponseDto;

import co.com.bancolombia.api.dto.product.request.ProductCreateRequestDto;
import co.com.bancolombia.api.dto.product.request.ProductUpdateNameRequestDto;
import co.com.bancolombia.api.dto.product.response.ProductResponseDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.product.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
public class ProductHandler {

    private final ProductUseCase productUseCase;
    private final ObjectMapper objectMapper;
    private final ValidationHelper validationHelper;

    public ProductHandler(ProductUseCase productUseCase, ObjectMapper objectMapper, ValidationHelper validationHelper) {
        this.productUseCase = productUseCase;
        this.objectMapper = objectMapper;
        this.validationHelper = validationHelper;
    }


    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(ProductCreateRequestDto.class)
                .doOnNext(validationHelper::validate)
                .map(dto -> objectMapper.convertValue(dto, Product.class))
                .flatMap(productUseCase::saveProduct)
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(response));

    }

    public Mono<ServerResponse> updateNameProduct(ServerRequest request) {
        return request.bodyToMono(ProductUpdateNameRequestDto.class)
                .doOnNext(validationHelper::validate)
                .flatMap(dto ->productUseCase.updateProductName(dto.getProductId(),dto.getName()))
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(response));

    }


    private ProductResponseDto mapToResponseDto(Product product) {
        return objectMapper.convertValue(product, ProductResponseDto.class);
    }
}
