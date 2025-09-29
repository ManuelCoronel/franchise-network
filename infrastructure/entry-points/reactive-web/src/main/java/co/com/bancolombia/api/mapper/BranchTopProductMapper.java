package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.inventory.response.BranchTopProductResponseDto;
import co.com.bancolombia.model.inventory.BranchTopProduct;

public class BranchTopProductMapper {

    public static BranchTopProductResponseDto toResponseDto(BranchTopProduct domain) {
        return new BranchTopProductResponseDto(
                domain.getBranchId(),
                domain.getBranchName(),
                domain.getProductId(),
                domain.getProductName(),
                domain.getStock()
        );
    }
}