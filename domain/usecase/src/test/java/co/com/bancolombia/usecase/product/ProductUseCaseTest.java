package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.constants.ErrorCodes;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static co.com.bancolombia.model.constants.ErrorCodes.PRODUCT_NOT_FOUND;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductUseCase productUseCase;

    private Product product;

    @BeforeEach
    void setUp() {

        Branch branchA = new Branch();
        branchA.setId(1L);
        branchA.setFranchiseId(100L);
        branchA.setName("Branch A");

        Branch branchB = new Branch();
        branchB.setId(2L);
        branchB.setFranchiseId(101L);
        branchB.setName("Branch B");

        List<Branch> branches = Arrays.asList(branchA, branchB);

        product = new Product();
        product.setId(100L);
        product.setName("Old Name");
        product.setBranches(branches);
    }


    @Test
    void saveProduct_ShouldReturnSavedProduct() {

        when(productRepository.save(product)).thenReturn(Mono.just(product));

        StepVerifier.create(productUseCase.saveProduct(product))
                .expectNextMatches(p ->
                        p.getId().equals(100L) && p.getBranches().size() == 2
                )
                .verifyComplete();

        verify(productRepository, times(1)).save(product);
    }


    @Test
    void getProductById_ShouldReturnProduct_WhenFound() {
        // Arrange
        Long productId = 100L;
        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        // Act & Assert
        StepVerifier.create(productUseCase.getProductById(productId))
                .expectNextMatches(p ->
                        p.getName().equals("Old Name") && p.getBranches().get(0).getName().equals("Branch A")
                )
                .verifyComplete();
    }

    @Test
    void getProductById_ShouldThrowResourceNotFoundException_WhenNotFound() {

        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Mono.empty());


        StepVerifier.create(productUseCase.getProductById(productId))
                .expectErrorSatisfies(throwable -> {
                    assert (throwable instanceof ResourceNotFoundException);
                    ResourceNotFoundException ex = (ResourceNotFoundException) throwable;
                    assert (ex.getCode() == PRODUCT_NOT_FOUND);
                })
                .verify();
    }


    @Test
    void updateProductName_ShouldUpdateNameAndKeepBranches_WhenFound() {

        Long productId = 100L;
        String newName = "Product New Name";


        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        Product updatedProduct = new Product();
        updatedProduct.setId(product.getId());
        updatedProduct.setName(newName);
        updatedProduct.setBranches(product.getBranches());

        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(updatedProduct));


        StepVerifier.create(productUseCase.updateProductName(productId, newName))
                .expectNextMatches(result ->
                        result.getName().equals(newName) &&
                                result.getBranches().size() == 2 &&
                                result.getBranches().get(1).getName().equals("Branch B")
                )
                .verifyComplete();

        verify(productRepository, times(1)).save(argThat(p ->
                p.getName().equals(newName) && p.getBranches().size() == 2
        ));
    }

    @Test
    void updateProductName_ShouldThrowException_WhenProductNotFound() {

        Long productId = 999L;
        String newName = "Test Name";

        when(productRepository.findById(productId)).thenReturn(Mono.empty());


        StepVerifier.create(productUseCase.updateProductName(productId, newName))
                .expectError(ResourceNotFoundException.class)
                .verify();
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).save(any(Product.class));
    }

}