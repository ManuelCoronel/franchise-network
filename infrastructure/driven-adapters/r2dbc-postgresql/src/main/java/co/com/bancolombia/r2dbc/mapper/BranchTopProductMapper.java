package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.inventory.BranchTopProduct;
import co.com.bancolombia.r2dbc.projection.BranchTopProductProjection;

public class BranchTopProductMapper {

    public static BranchTopProduct toDomain(BranchTopProductProjection projection) {
        return new BranchTopProduct(
                projection.getBranchId(),
                projection.getBranchName(),
                projection.getProductId(),
                projection.getProductName(),
                projection.getStock()
        );
    }
}