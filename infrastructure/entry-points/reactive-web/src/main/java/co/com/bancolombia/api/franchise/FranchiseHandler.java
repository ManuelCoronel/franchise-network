package co.com.bancolombia.api.franchise;

import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.franchise.request.FranchiseCreateRequestDto;
import co.com.bancolombia.api.dto.franchise.request.FranchiseUpdateNameRequestDto;
import co.com.bancolombia.api.dto.franchise.response.FranchiseResponseDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FranchiseHandler {

    private final FranchiseUseCase franchiseUseCase;
    private final ValidationHelper validationHelper;
    private final ObjectMapper objectMapper;

    public FranchiseHandler(FranchiseUseCase franchiseUseCase, ValidationHelper validationHelper, ObjectMapper objectMapper) {
        this.franchiseUseCase = franchiseUseCase;
        this.validationHelper = validationHelper;
        this.objectMapper = objectMapper;
    }


    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseCreateRequestDto.class)
                .doOnNext(validationHelper::validate)
                .map(dto -> objectMapper.convertValue(dto, Franchise.class))
                .flatMap(franchiseUseCase::saveFranchise)
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response));

    }

    public Mono<ServerResponse> updateNameFranchise(ServerRequest request) {
        return request.bodyToMono(FranchiseUpdateNameRequestDto.class)
                .doOnNext(validationHelper::validate)
                .flatMap(dto ->franchiseUseCase.updateNameFranchise(dto.getFranchiseId(),dto.getName()))
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response));

    }



    private FranchiseResponseDto mapToResponseDto(Franchise franchise) {
        return objectMapper.convertValue(franchise, FranchiseResponseDto.class);
    }
}
