package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.mapper.BranchTopProductMapper;
import co.com.bancolombia.r2dbc.repository.FranchiseRepositoryDB;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class FranchiseRepositoryAdapter
        extends ReactiveAdapterOperations<Franchise, FranchiseEntity, Long, FranchiseRepositoryDB>
        implements FranchiseRepository {



    public FranchiseRepositoryAdapter(FranchiseRepositoryDB repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }


}