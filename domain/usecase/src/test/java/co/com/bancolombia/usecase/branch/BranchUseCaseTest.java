package co.com.bancolombia.usecase.branch;


import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.model.constants.ErrorCodes.BRANCH_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BranchUseCaseTest {

    private BranchRepository branchRepository;
    private FranchiseUseCase franchiseUseCase;
    private BranchUseCase branchUseCase;

    @BeforeEach
    void setup() {
        branchRepository = Mockito.mock(BranchRepository.class);
        franchiseUseCase = Mockito.mock(FranchiseUseCase.class);
        branchUseCase = new BranchUseCase(branchRepository, franchiseUseCase);
    }

    @Test
    void shouldSaveBranchWhenFranchiseExists() {
        Branch branch = new Branch();
        branch.setFranchiseId(1L);

        Franchise franchise = new Franchise();
        franchise.setId(1L);

        when(franchiseUseCase.getFranchise(1L)).thenReturn(Mono.just(franchise));
        when(branchRepository.save(branch)).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.save(branch))
                .expectNext(branch)
                .verifyComplete();

        verify(branchRepository).save(branch);
    }

    @Test
    void shouldReturnBranchWhenFoundById() {
        Branch branch = new Branch();
        branch.setId(100L);

        when(branchRepository.findById(100L)).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.getBranchById(100L))
                .expectNext(branch)
                .verifyComplete();
    }

    @Test
    void shouldThrowErrorWhenBranchNotFound() {
        when(branchRepository.findById(200L)).thenReturn(Mono.empty());

        StepVerifier.create(branchUseCase.getBranchById(200L))
                .expectErrorSatisfies(error -> {
                    assert error instanceof ResourceNotFoundException;
                    ResourceNotFoundException ex = (ResourceNotFoundException) error;
                    assert ex.getCode().equals(BRANCH_NOT_FOUND);
                })
                .verify();
    }

    @Test
    void shouldUpdateBranchName() {
        Branch branch = new Branch();
        branch.setId(300L);
        branch.setFranchiseId(2L);
        branch.setName("Old Name");

        Franchise franchise = new Franchise();
        franchise.setId(1L);

        when(branchRepository.findById(300L)).thenReturn(Mono.just(branch));
        when(franchiseUseCase.getFranchise(2L)).thenReturn(Mono.just(franchise));
        when(branchRepository.save(any(Branch.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(branchUseCase.updateNameBranch(300L, "New Name"))
                .assertNext(updatedBranch -> {
                    assert updatedBranch.getName().equals("New Name");
                })
                .verifyComplete();

        verify(branchRepository).save(any(Branch.class));
    }
}
