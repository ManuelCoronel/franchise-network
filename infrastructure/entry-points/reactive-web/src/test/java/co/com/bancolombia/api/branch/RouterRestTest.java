package co.com.bancolombia.api.branch;

import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RouterRestTest {

    private BranchHandler branchHandler;
    private WebTestClient webTestClient;
    private BranchUseCase branchUseCase;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        branchUseCase = mock(BranchUseCase.class);

        branchHandler = new BranchHandler(branchUseCase,new ValidationHelper(validator),new ObjectMapper());
        RouterRest routerRest = new RouterRest();
        RouterFunction<ServerResponse> route = routerRest.routerBranchFunction(branchHandler);

        webTestClient = WebTestClient.bindToRouterFunction(route).build();
    }

    @Test
    void whenPostBranch_thenHandlerIsCalled() {
        // Arrange
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Branch One");


        when(branchUseCase.save(any()))
                .thenReturn(Mono.just(branch));

        // Act & Assert
        webTestClient.post()
                .uri("/api/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"franchiseId\":1,\"name\":\"Branch One\"}")
                .exchange()
                .expectStatus().isOk();

        verify(branchHandler, times(1)).createBranch(any());
    }

    @Test
    void whenPatchBranch_thenHandlerIsCalled() {
        // Arrange
        String newNameBranch = "Branch new name";

        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName(newNameBranch);


        when(branchUseCase.updateNameBranch(1L,newNameBranch))
                .thenReturn(Mono.just(branch));
        // Act & Assert
        webTestClient.patch()
                .uri("/api/branch")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"branchId\":1,\"name\":\"Branch new name\"}")
                .exchange()
                .expectStatus().isOk();


    }
}
