package co.com.bancolombia.r2dbc.repository;


import co.com.bancolombia.r2dbc.entity.InventoryEntity;
import co.com.bancolombia.r2dbc.projection.BranchTopProductProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryRepositoryDB extends ReactiveCrudRepository<InventoryEntity, Long>, ReactiveQueryByExampleExecutor<InventoryEntity> {

    @Query("DELETE FROM inventory WHERE product_id = :productId AND branch_id = :branchId")
    Mono<Void> deleteByProductIdAndBranchId(Long productId, Long branchId);

    @Query("""
    SELECT b.id AS branch_id,
           b.name AS branch_name,
           p.id AS product_id,
           p.name AS product_name,
           i.stock AS stock
    FROM branch b
    JOIN inventory i ON b.id = i.branch_id
    JOIN product p  ON i.product_id = p.id
    WHERE b.franchise_id = :franchiseId
      AND i.stock = (
          SELECT MAX(i2.stock)
          FROM inventory i2
          WHERE i2.branch_id = b.id
      )
    """)
    Flux<BranchTopProductProjection> findTopProductsByFranchise(Long franchiseId);

    Mono<InventoryEntity> findByProductIdAndBranchId(Long productId, Long branchId);


}
