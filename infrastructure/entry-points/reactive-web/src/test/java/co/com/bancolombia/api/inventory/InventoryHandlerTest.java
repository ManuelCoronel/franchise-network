package co.com.bancolombia.api.inventory;


import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.inventory.request.InventoryCreateRequestDto;
import co.com.bancolombia.api.dto.inventory.request.InventoryUpdateRequestDto;
import co.com.bancolombia.api.dto.inventory.response.InventoryCreateResponseDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.api.mapper.BranchTopProductMapper;
import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.model.inventory.Inventory;
import co.com.bancolombia.usecase.inventory.InventoryUseCase;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryHandlerTest {

    @Mock
    private InventoryUseCase inventoryUseCase;


    private ValidationHelper validationHelper;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private InventoryHandler inventoryHandler;

    private InventoryCreateRequestDto createRequestDto;
    private InventoryUpdateRequestDto updateRequestDto;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        createRequestDto = new InventoryCreateRequestDto();
        createRequestDto.setBranchId(10L);
        createRequestDto.setProductId(20L);
        createRequestDto.setStock(50);

        updateRequestDto = new InventoryUpdateRequestDto();
        updateRequestDto.setBranchId(10L);
        updateRequestDto.setProductId(20L);
        updateRequestDto.setStock(100L);

        inventory = new Inventory();
        inventory.setBranchId(10L);
        inventory.setProductId(20L);
        inventory.setStock(50L);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        objectMapper= new ObjectMapper();
        this.validationHelper = new ValidationHelper(validator);

        inventoryHandler = new InventoryHandler(inventoryUseCase, objectMapper, validationHelper);
    }


    @Test
    void createInventory_success() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(InventoryCreateRequestDto.class)).thenReturn(Mono.just(createRequestDto));
        when(inventoryUseCase.save(any(Inventory.class))).thenReturn(Mono.just(inventory));

        // When
        Mono<ServerResponse> response = inventoryHandler.createInventory(request);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();


        verify(inventoryUseCase, times(1)).save(any(Inventory.class));
    }



    @Test
    void getTopProductsByFranchise_success() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("franchiseId")).thenReturn("5");

        BranchTopProduct topProduct1 = new BranchTopProduct(
                10L, "Branch A", 100L, "Laptop", 30L
        );

        BranchTopProduct topProduct2 = new BranchTopProduct(
                11L, "Branch B", 101L, "Mouse", 25L
        );

        when(inventoryUseCase.findTopProductsByFranchise(5L))
                .thenReturn(Flux.just(topProduct1, topProduct2));

        // When
        Mono<ServerResponse> response = inventoryHandler.getTopProductsByFranchise(request);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(inventoryUseCase, times(1)).findTopProductsByFranchise(5L);
    }

    @Test
    void removeInventory_success() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.pathVariable("branchId")).thenReturn("10");
        when(request.pathVariable("productId")).thenReturn("20");

        when(inventoryUseCase.removeProductFromBranch(10L, 20L))
                .thenReturn(Mono.empty());

        // When
        Mono<ServerResponse> response = inventoryHandler.removeInventory(request);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();

        verify(inventoryUseCase, times(1)).removeProductFromBranch(10L, 20L);
    }

    @Test
    void updateStockFromProductOfBranch_success() {
        // Given
        ServerRequest request = mock(ServerRequest.class);
        when(request.bodyToMono(InventoryUpdateRequestDto.class)).thenReturn(Mono.just(updateRequestDto));
        when(inventoryUseCase.updateStockFromProductOfBranch(any(Inventory.class)))
                .thenReturn(Mono.just(inventory));

        // When
        Mono<ServerResponse> response = inventoryHandler.updateStockFromProductOfBranch(request);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(inventoryUseCase, times(1)).updateStockFromProductOfBranch(any(Inventory.class));
    }

}
