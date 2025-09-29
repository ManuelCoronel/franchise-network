package co.com.bancolombia.api.branch;

import co.com.bancolombia.api.dto.branch.request.BranchCreateRequestDto;
import co.com.bancolombia.api.dto.branch.request.BranchUpdateNameRequestDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.branch.BranchUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BranchHandlerTest {
    @Mock
    private BranchUseCase branchUseCase;

    private ObjectMapper objectMapper;

    private ValidationHelper validationHelper;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private BranchHandler branchHandler;


    private final Branch branchDomain = new Branch();
    private final String  branch = "Branch A";

    @BeforeEach
    void setup() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        objectMapper= new ObjectMapper();
        this.validationHelper = new ValidationHelper(validator);


        this.branchHandler = new BranchHandler(
                this.branchUseCase,
                this.validationHelper,
                this.objectMapper);

        branchDomain.setName("new Name");

    }

    @Test
    void createBranch_ShouldReturnOkResponse_WhenSuccessful() {

        BranchCreateRequestDto requestDto = new BranchCreateRequestDto();
        requestDto.setName(branch);
        requestDto.setFranchiseId(1L);

        when(serverRequest.bodyToMono(BranchCreateRequestDto.class)).thenReturn(Mono.just(requestDto));
        when(branchUseCase.save(any(Branch.class))).thenReturn(Mono.just(branchDomain));
        Mono<ServerResponse> responseMono = branchHandler.createBranch(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.OK)
                )
                .verifyComplete();
    }

    @Test
    void updateNameBranch_ShouldReturnOkResponse_WhenSuccessful() {

        BranchUpdateNameRequestDto updateRequestDto = new BranchUpdateNameRequestDto();

        updateRequestDto.setName(branch);
        updateRequestDto.setBranchId(1L);

        when(serverRequest.bodyToMono(BranchUpdateNameRequestDto.class)).thenReturn(Mono.just(updateRequestDto));


        when(branchUseCase.updateNameBranch(any(),any())).thenReturn(Mono.just(branchDomain));
        Mono<ServerResponse> responseMono = branchHandler.updateNameBranch(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().equals(HttpStatus.OK)
                )
                .verifyComplete();
    }



}