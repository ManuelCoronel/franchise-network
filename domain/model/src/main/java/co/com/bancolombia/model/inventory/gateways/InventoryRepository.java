package co.com.bancolombia.model.inventory.gateways;

import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.model.inventory.Inventory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryRepository {

    Mono<Inventory> save(Inventory inventory);

    Mono<Void> deleteById(Long id);

    Mono<Void> deleteByProductIdAndBranchId(Long productId, Long branchId);

    Mono<Inventory> findByProductIdAndBranchId(Long productId, Long branchId);

    Flux<BranchTopProduct> findTopProductsByFranchise(Long franchiseId);

}
