package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.exception.ResourceNotFoundException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import reactor.core.publisher.Mono;




import static co.com.bancolombia.model.constants.ErrorCodes.PRODUCT_NOT_FOUND;

public class ProductUseCase {

    private final ProductRepository productRepository;

    public ProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> saveProduct(Product product){
        return this.productRepository.save(product);
    }

    public Mono<Product> getProductById(Long productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(
                        Mono.error(new ResourceNotFoundException(
                                "The product with ID: " + productId + " does not exist",
                                PRODUCT_NOT_FOUND
                        ))
                );
    }

    public Mono<Product> updateProductName(Long productId, String name){
        return this.getProductById(productId).flatMap(
                product -> {
                    product.setName(name);
                    return this.saveProduct(product);
                }
        );


    }


}
