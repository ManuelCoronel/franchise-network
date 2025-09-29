package co.com.bancolombia.api.franchise;

import co.com.bancolombia.api.franchise.FranchiseHandler;
import co.com.bancolombia.api.franchise.RouterRest;
import co.com.bancolombia.api.dto.franchise.request.FranchiseCreateRequestDto;
import co.com.bancolombia.api.dto.franchise.request.FranchiseUpdateNameRequestDto;
import co.com.bancolombia.api.helper.ValidationHelper;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RouterRestTest {

    private WebTestClient webTestClient;
    private FranchiseUseCase franchiseUseCase;
    private FranchiseHandler franchiseHandler;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        franchiseUseCase = mock(FranchiseUseCase.class);
        franchiseHandler = new FranchiseHandler(franchiseUseCase,new ValidationHelper(validator), new ObjectMapper());

        RouterRest routerRest = new RouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerFunction(franchiseHandler)).build();
    }

    @Test
    void createFranchise_ShouldReturnCreated() {
        FranchiseCreateRequestDto requestDto = new FranchiseCreateRequestDto();
        requestDto.setName("Test Franchise");

        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        when(franchiseUseCase.saveFranchise(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("Test Franchise");
    }

    @Test
    void updateFranchise_ShouldReturnCreated() {
        FranchiseUpdateNameRequestDto requestDto = new FranchiseUpdateNameRequestDto();
        requestDto.setFranchiseId(1L);
        requestDto.setName("Updated Franchise");

        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Updated Franchise");

        when(franchiseUseCase.updateNameFranchise(1L, "Updated Franchise"))
                .thenReturn(Mono.just(franchise));

        webTestClient.put()
                .uri("/api/franchise")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo("Updated Franchise");
    }
}
