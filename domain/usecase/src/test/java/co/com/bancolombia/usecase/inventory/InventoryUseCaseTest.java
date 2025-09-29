package co.com.bancolombia.usecase.inventory;


import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.exception.ResourceAlreadyExistsException;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.model.inventory.Inventory;
import co.com.bancolombia.model.inventory.gateways.InventoryRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.branch.BranchUseCase;
import co.com.bancolombia.usecase.franchise.FranchiseUseCase;
import co.com.bancolombia.usecase.product.ProductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.model.constants.ErrorCodes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryUseCaseTest {

    private BranchUseCase branchUseCase;
    private ProductUseCase productUseCase;
    private FranchiseUseCase franchiseUseCase;
    private InventoryRepository inventoryRepository;

    private InventoryUseCase inventoryUseCase;

    @BeforeEach
    void setup() {
        branchUseCase = mock(BranchUseCase.class);
        productUseCase = mock(ProductUseCase.class);
        franchiseUseCase = mock(FranchiseUseCase.class);
        inventoryRepository = mock(InventoryRepository.class);

        inventoryUseCase = new InventoryUseCase(branchUseCase, productUseCase, franchiseUseCase, inventoryRepository);
    }

    @Test
    void shouldRemoveProductFromBranch() {
        when(inventoryRepository.deleteByProductIdAndBranchId(2L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(inventoryUseCase.removeProductFromBranch(1L, 2L))
                .verifyComplete();

        verify(inventoryRepository).deleteByProductIdAndBranchId(2L, 1L);
    }

    @Test
    void shouldUpdateStockWhenInventoryExists() {
        Inventory existing = new Inventory(1L, 1L, 2L, 10L);
        Inventory input = new Inventory(null, 1L, 2L, 20L);
        Inventory updated = new Inventory(1L, 1L, 2L, 20L);

        when(inventoryRepository.findByProductIdAndBranchId(2L, 1L)).thenReturn(Mono.just(existing));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(inventoryUseCase.updateStockFromProductOfBranch(input))
                .expectNextMatches(inv -> inv.getStock() == 20L)
                .verifyComplete();

        verify(inventoryRepository).findByProductIdAndBranchId(2L, 1L);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingInventory() {
        Inventory input = new Inventory(null, 1L, 2L, 20L);

        when(inventoryRepository.findByProductIdAndBranchId(2L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(inventoryUseCase.updateStockFromProductOfBranch(input))
                .expectErrorSatisfies(error -> {
                    assert error instanceof ResourceNotFoundException;
                    assert ((ResourceNotFoundException) error).getCode().equals(INVENTORY_NOT_FOUND);
                })
                .verify();

        verify(inventoryRepository).findByProductIdAndBranchId(2L, 1L);
    }

    @Test
    void shouldSaveInventoryWhenNotExists() {
        Inventory inventory = new Inventory(null, 1L, 2L, 15L);

        Branch branch = new Branch();
        Product product = new Product();

        when(inventoryRepository.findByProductIdAndBranchId(2L, 1L)).thenReturn(Mono.empty());
        when(branchUseCase.getBranchById(1L)).thenReturn(Mono.just(branch));
        when(productUseCase.getProductById(2L)).thenReturn(Mono.just(product));
        when(inventoryRepository.save(inventory)).thenReturn(Mono.just(inventory));

        StepVerifier.create(inventoryUseCase.save(inventory))
                .expectNext(inventory)
                .verifyComplete();

        verify(branchUseCase).getBranchById(1L);
        verify(productUseCase).getProductById(2L);
        verify(inventoryRepository).save(inventory);
    }

    @Test
    void shouldThrowWhenSavingAlreadyExistingInventory() {
        Inventory inventory = new Inventory(null, 1L, 2L, 15L);
        Branch branch = new Branch();
        Product product = new Product();

        when(inventoryRepository.findByProductIdAndBranchId(2L, 1L)).thenReturn(Mono.just(inventory));
        when(branchUseCase.getBranchById(1L)).thenReturn(Mono.just(branch));
        when(productUseCase.getProductById(2L)).thenReturn(Mono.just(product));
        StepVerifier.create(inventoryUseCase.save(inventory))
                .expectErrorSatisfies(error -> {
                    assert error instanceof ResourceAlreadyExistsException;
                    assert ((ResourceAlreadyExistsException) error).getCode().equals(INVENTORY_ALREADY_EXITS);
                })
                .verify();

        verify(inventoryRepository).findByProductIdAndBranchId(2L, 1L);
    }

    @Test
    void shouldFindTopProductsByFranchise() {
        BranchTopProduct top = new BranchTopProduct(0L,"Coca cola branch",0L,"coca cola product",0L);

        Franchise franchise = new Franchise();

        when(franchiseUseCase.getFranchise(5L)).thenReturn(Mono.just(franchise));
        when(inventoryRepository.findTopProductsByFranchise(5L)).thenReturn(Flux.just(top));

        StepVerifier.create(inventoryUseCase.findTopProductsByFranchise(5L))
                .expectNext(top)
                .verifyComplete();

        verify(franchiseUseCase).getFranchise(5L);
        verify(inventoryRepository).findTopProductsByFranchise(5L);
    }
}
