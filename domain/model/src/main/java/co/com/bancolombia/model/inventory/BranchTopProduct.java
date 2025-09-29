package co.com.bancolombia.model.inventory;

import lombok.Value;

@Value
public class BranchTopProduct {
    Long branchId;
    String branchName;
    Long productId;
    String productName;
    Long stock;
}