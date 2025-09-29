package co.com.bancolombia.usecase.inventory;

import co.com.bancolombia.model.exception.ResourceAlreadyExistsException;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.model.inventory.Inventory;
import co.com.bancolombia.model.inventory.gateways.InventoryRepository;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import co.com.bancolombia.usecase.product.ProductUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.constants.ErrorCodes.*;


public class InventoryUseCase {


    private final BranchUseCase branchUseCase;
    private final ProductUseCase productUseCase;
    private final FranchiseUseCase franchiseUseCase;
    private final InventoryRepository inventoryRepository;

    public InventoryUseCase(BranchUseCase branchUseCase, ProductUseCase productUseCase, FranchiseUseCase franchiseUseCase, InventoryRepository inventoryRepository) {
        this.branchUseCase = branchUseCase;
        this.productUseCase = productUseCase;
        this.franchiseUseCase = franchiseUseCase;
        this.inventoryRepository = inventoryRepository;
    }


    public Mono<Void> removeProductFromBranch(Long branchId, Long productId){
        return inventoryRepository.deleteByProductIdAndBranchId(productId,branchId);
    }

    public Mono<Inventory> updateStockFromProductOfBranch(Inventory inventory){
        Long branchId = inventory.getBranchId();
        Long productId = inventory.getProductId();

        return
                inventoryRepository.findByProductIdAndBranchId(productId, branchId)
                .flatMap(
                        existingInventory -> {
                            existingInventory.setStock(inventory.getStock());
                            return inventoryRepository.save(existingInventory);
                        }
                ).switchIfEmpty(
                                Mono.error(new ResourceNotFoundException(
                                        "The inventory with branch ID: " + branchId + " and product ID "+ productId +" does not exist",
                                        INVENTORY_NOT_FOUND
                                ))

                        );
    }

    public Mono<Object> save(Inventory inventory) {
        Long branchId = inventory.getBranchId();
        Long productId = inventory.getProductId();

        return inventoryRepository.findByProductIdAndBranchId(productId, branchId)
                .flatMap(existingInventory ->
                        Mono.error(
                                new ResourceAlreadyExistsException("The inventory with branch ID: " + branchId + " and product ID " + productId + " already exists", INVENTORY_ALREADY_EXITS)
                        )
                ).switchIfEmpty(
                        branchUseCase.getBranchById(branchId)
                                .then(productUseCase.getProductById(productId))
                                .then(Mono.just(inventory))
                                .flatMap(inventoryRepository::save)
                );
    }




    public Flux<BranchTopProduct> findTopProductsByFranchise(Long franchiseId){
        return franchiseUseCase.getFranchise(franchiseId).thenMany(
                inventoryRepository.findTopProductsByFranchise(franchiseId));
    }

}