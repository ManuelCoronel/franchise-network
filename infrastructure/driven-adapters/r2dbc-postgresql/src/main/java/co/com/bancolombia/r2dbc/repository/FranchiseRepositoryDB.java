package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.projection.BranchTopProductProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FranchiseRepositoryDB extends ReactiveCrudRepository<FranchiseEntity, Long>, ReactiveQueryByExampleExecutor<FranchiseEntity> {


}

