package co.com.bancolombia.model.product;
import co.com.bancolombia.model.branch.Branch;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Product {
    private Long id;
    private String name;
    private List<Branch> branches;
}
