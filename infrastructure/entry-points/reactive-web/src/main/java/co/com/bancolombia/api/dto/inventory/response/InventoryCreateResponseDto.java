package co.com.bancolombia.api.dto.inventory.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryCreateResponseDto {

    private Long id;
    private Long branchId;
    private Long productId;
    private Integer stock;

}
