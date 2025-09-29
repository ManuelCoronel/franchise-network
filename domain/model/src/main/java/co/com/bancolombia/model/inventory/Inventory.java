package co.com.bancolombia.model.inventory;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    private Long id;
    private Long branchId;
    private Long productId;
    private Long stock;

}
