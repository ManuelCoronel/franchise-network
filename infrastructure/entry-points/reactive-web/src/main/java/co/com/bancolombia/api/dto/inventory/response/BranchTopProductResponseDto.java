package co.com.bancolombia.api.dto.inventory.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchTopProductResponseDto {
    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private Long stock;
}