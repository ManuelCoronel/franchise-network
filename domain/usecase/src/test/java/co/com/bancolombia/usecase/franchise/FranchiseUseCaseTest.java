package co.com.bancolombia.usecase.franchise;


import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.model.constants.ErrorCodes.FRANCHISE_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FranchiseUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setup() {
        franchiseRepository = mock(FranchiseRepository.class);
        franchiseUseCase = new FranchiseUseCase(franchiseRepository);
    }

    @Test
    void shouldSaveFranchise() {
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Franchise 1");

        when(franchiseRepository.save(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.saveFranchise(franchise))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository).save(franchise);
    }

    @Test
    void shouldReturnFranchiseWhenFoundById() {
        Franchise franchise = new Franchise();
        franchise.setId(2L);
        franchise.setName("Franchise 2");

        when(franchiseRepository.findById(2L)).thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.getFranchise(2L))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository).findById(2L);
    }

    @Test
    void shouldThrowErrorWhenFranchiseNotFound() {
        when(franchiseRepository.findById(3L)).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUseCase.getFranchise(3L))
                .expectErrorSatisfies(error -> {
                    assert error instanceof ResourceNotFoundException;
                    ResourceNotFoundException ex = (ResourceNotFoundException) error;
                    assert ex.getCode().equals(FRANCHISE_NOT_FOUND);
                    assert ex.getMessage().contains("The franchise with ID 3 does not exist");
                })
                .verify();

        verify(franchiseRepository).findById(3L);
    }

    @Test
    void shouldUpdateFranchiseName() {
        Franchise franchise = new Franchise();
        franchise.setId(4L);
        franchise.setName("Old Name");

        when(franchiseRepository.findById(4L)).thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(franchiseUseCase.updateNameFranchise(4L, "New Name"))
                .assertNext(updated -> {
                    assert updated.getName().equals("New Name");
                })
                .verifyComplete();

        verify(franchiseRepository).findById(4L);
        verify(franchiseRepository).save(any(Franchise.class));
    }
}
