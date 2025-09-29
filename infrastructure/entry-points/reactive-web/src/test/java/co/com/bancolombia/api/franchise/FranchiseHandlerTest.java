package co.com.bancolombia.api.franchise;

import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.franchise.request.FranchiseCreateRequestDto;
import co.com.bancolombia.api.dto.franchise.request.FranchiseUpdateNameRequestDto;
import co.com.bancolombia.api.dto.franchise.response.FranchiseResponseDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FranchiseHandlerTest {

    @Mock
    private FranchiseUseCase franchiseUseCase;

    private ObjectMapper objectMapper;

    private ValidationHelper validationHelper;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private FranchiseHandler franchiseHandler;


    private final Franchise franchiseDomain = new Franchise();
    private final String  franchise = "Franchise A";

    @BeforeEach
    void setup() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        objectMapper= new ObjectMapper();
        this.validationHelper = new ValidationHelper(validator);


        this.franchiseHandler = new FranchiseHandler(
                this.franchiseUseCase,
                this.validationHelper,
                this.objectMapper);

        franchiseDomain.setName("new Name");

    }

        @Test
    void createFranchise_ShouldReturnOkResponse_WhenSuccessful() {

        FranchiseCreateRequestDto requestDto = new FranchiseCreateRequestDto();
        requestDto.setName(franchise);

        when(serverRequest.bodyToMono(FranchiseCreateRequestDto.class)).thenReturn(Mono.just(requestDto));
        when(franchiseUseCase.saveFranchise(any(Franchise.class))).thenReturn(Mono.just(franchiseDomain));
        Mono<ServerResponse> responseMono = franchiseHandler.createFranchise(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.OK)
                )
                .verifyComplete();
    }

    @Test
    void updateNameFranchise_ShouldReturnOkResponse_WhenSuccessful() {

        FranchiseUpdateNameRequestDto updateRequestDto = new FranchiseUpdateNameRequestDto();

        updateRequestDto.setName(franchise);
        updateRequestDto.setFranchiseId(1L);

        when(serverRequest.bodyToMono(FranchiseUpdateNameRequestDto.class)).thenReturn(Mono.just(updateRequestDto));


        when(franchiseUseCase.updateNameFranchise(any(),any())).thenReturn(Mono.just(franchiseDomain));
        Mono<ServerResponse> responseMono = franchiseHandler.updateNameFranchise(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.OK)
                )
                .verifyComplete();
    }



}