package co.com.bancolombia.api.branch;


import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.branch.request.BranchCreateRequestDto;
import co.com.bancolombia.api.dto.branch.request.BranchUpdateNameRequestDto;
import co.com.bancolombia.api.dto.branch.response.BranchResponseDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class BranchHandler {

    private final BranchUseCase branchUseCase;
    private final ValidationHelper validationHelper;
    private final ObjectMapper objectMapper;

    public BranchHandler(BranchUseCase branchUseCase, ValidationHelper validationHelper, ObjectMapper objectMapper) {
        this.branchUseCase = branchUseCase;
        this.validationHelper = validationHelper;
        this.objectMapper = objectMapper;
    }

    public Mono<ServerResponse> createBranch(ServerRequest request) {
        return request.bodyToMono(BranchCreateRequestDto.class)
                .doOnNext(validationHelper::validate)
                .map(dto -> objectMapper.convertValue(dto, Branch.class))
                .flatMap(branchUseCase::save)
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response));

    }

    public Mono<ServerResponse> updateNameBranch(ServerRequest request) {
        return request.bodyToMono(BranchUpdateNameRequestDto.class)
                .doOnNext(validationHelper::validate)
                .flatMap(dto->branchUseCase.updateNameBranch(dto.getBranchId(),dto.getName()))
                .map(this::mapToResponseDto)
                .map(ApiResponseDto::ok)
                .flatMap(response -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(response));

    }

    private BranchResponseDto mapToResponseDto(Branch branch) {
        return objectMapper.convertValue(branch, BranchResponseDto.class);
    }
}