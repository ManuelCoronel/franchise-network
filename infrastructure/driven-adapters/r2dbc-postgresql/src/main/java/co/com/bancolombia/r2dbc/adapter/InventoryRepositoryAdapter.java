package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.model.inventory.Inventory;
import co.com.bancolombia.model.inventory.gateways.InventoryRepository;
import co.com.bancolombia.r2dbc.entity.InventoryEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.mapper.BranchTopProductMapper;
import co.com.bancolombia.r2dbc.repository.InventoryRepositoryDB;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class InventoryRepositoryAdapter extends ReactiveAdapterOperations<
        Inventory,
        InventoryEntity,
        Long,
        InventoryRepositoryDB>
        implements InventoryRepository {



    public InventoryRepositoryAdapter(InventoryRepositoryDB repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Inventory.class));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return this.repository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteByProductIdAndBranchId(Long productId, Long branchId) {
        return repository.deleteByProductIdAndBranchId(productId,branchId);
    }

    @Override
    public Mono<Inventory> findByProductIdAndBranchId(Long productId, Long branchId) {
        return this.repository.findByProductIdAndBranchId(productId,branchId).map(
             inventory->  this.mapper.map(inventory,Inventory.class)
        );
    }

    @Override
    public Flux<BranchTopProduct> findTopProductsByFranchise(Long franchiseId) {
        return this.repository.findTopProductsByFranchise(franchiseId).map(BranchTopProductMapper::toDomain);
    }
}