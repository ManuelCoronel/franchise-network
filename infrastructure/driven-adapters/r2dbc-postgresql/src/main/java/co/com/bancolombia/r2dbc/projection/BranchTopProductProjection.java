package co.com.bancolombia.r2dbc.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchTopProductProjection {

    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private Long stock;

}