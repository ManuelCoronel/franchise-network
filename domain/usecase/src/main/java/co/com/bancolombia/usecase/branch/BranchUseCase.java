package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.constants.ErrorCodes.BRANCH_NOT_FOUND;
import static co.com.bancolombia.model.constants.ErrorCodes.FRANCHISE_NOT_FOUND;


public class BranchUseCase {

    private final BranchRepository branchRepository;
    private final FranchiseUseCase franchiseUseCase;

    public BranchUseCase(BranchRepository branchRepository, FranchiseUseCase franchiseUseCase) {
        this.branchRepository = branchRepository;
        this.franchiseUseCase = franchiseUseCase;
    }


    public Mono<Branch> save(Branch branch) {
        Long franchiseId = branch.getFranchiseId();

        return franchiseUseCase.getFranchise(franchiseId)
                .flatMap(franchise ->
                        branchRepository.save(branch));
    }


    public Mono<Branch> getBranchById(Long branchId) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException(
                                "The branch with ID: " + branchId + " does not exist",
                                BRANCH_NOT_FOUND
                        ))
                );

    }

    public Mono<Branch> updateNameBranch(Long branchId, String name)
    {
        return this.getBranchById(branchId).flatMap(
                branch ->
                {
                    branch.setName(name);
                    return this.save(branch);
                }
        );

    }

}