package co.com.bancolombia.usecase.franchise;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.constants.ErrorCodes.FRANCHISE_NOT_FOUND;


public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public FranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Franchise> saveFranchise(Franchise franchise){
        return franchiseRepository.save(franchise);
    }

    public Mono<Franchise> getFranchise(Long franchiseId){
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException(
                                "The franchise with ID " + franchiseId + " does not exist",
                                FRANCHISE_NOT_FOUND
                        ))
                );
    }

    public Mono<Franchise> updateNameFranchise(Long franchiseId, String name){
        return this.getFranchise(franchiseId).flatMap(
                franchise ->
                {
                    franchise.setName(name);
                    return this.saveFranchise(franchise);
                }

        );

    }

}
