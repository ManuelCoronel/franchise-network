package co.com.bancolombia.api.dto.inventory.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryUpdateRequestDto {

    @NotNull(message = "BranchId cannot be null")
    @Min(value = 0, message = "BranchId must be a positive number")
    private Long branchId;
    @NotNull(message = "ProductId cannot be null")
    @Min(value = 0, message = "ProductId must be a positive number")
    private Long productId;
    @Min(value = 1, message = "Stock must be a positive number")
    private Long stock;
}
