package co.com.bancolombia.api.inventory;

import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.inventory.request.InventoryCreateRequestDto;
import co.com.bancolombia.api.dto.inventory.request.InventoryUpdateRequestDto;
import co.com.bancolombia.api.dto.inventory.response.InventoryCreateResponseDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.api.mapper.BranchTopProductMapper;
import co.com.bancolombia.model.inventory.Inventory;
import co.com.bancolombia.usecase.inventory.InventoryUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
public class InventoryHandler {

    private final InventoryUseCase inventoryUseCase;
    private final ObjectMapper objectMapper;
    private final ValidationHelper validationHelper;

    public InventoryHandler(InventoryUseCase inventoryUseCase, ObjectMapper objectMapper, ValidationHelper validationHelper) {
        this.inventoryUseCase = inventoryUseCase;
        this.objectMapper = objectMapper;
        this.validationHelper = validationHelper;
    }

    public Mono<ServerResponse> createInventory(ServerRequest request) {
        return request.bodyToMono(InventoryCreateRequestDto.class)
                .doOnNext(validationHelper::validate)
                .map(dto -> objectMapper.convertValue(dto, Inventory.class))
                .flatMap(inventoryUseCase::save)
                .map(object -> objectMapper.convertValue(object, Inventory.class))
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(response));

    }

    public Mono<ServerResponse> getTopProductsByFranchise(ServerRequest request) {
        Long franchiseId = Long.valueOf(request.pathVariable("franchiseId"));

        return inventoryUseCase.findTopProductsByFranchise(franchiseId)
                .map(BranchTopProductMapper::toResponseDto)
                .collectList()
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response));
    }

    public Mono<ServerResponse> removeInventory(ServerRequest request) {
        Long branchId = Long.valueOf(request.pathVariable("branchId"));
        Long productId = Long.valueOf(request.pathVariable("productId"));

        return inventoryUseCase.removeProductFromBranch(branchId, productId)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateStockFromProductOfBranch(ServerRequest request) {
        return request.bodyToMono(InventoryUpdateRequestDto.class)
                .doOnNext(validationHelper::validate)
                .map(dto -> objectMapper.convertValue(dto, Inventory.class))
                .flatMap(inventoryUseCase::updateStockFromProductOfBranch)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response));
    }


    private InventoryCreateResponseDto mapToResponseDto(Inventory inventory) {
        return objectMapper.convertValue(inventory, InventoryCreateResponseDto.class);
    }
}
