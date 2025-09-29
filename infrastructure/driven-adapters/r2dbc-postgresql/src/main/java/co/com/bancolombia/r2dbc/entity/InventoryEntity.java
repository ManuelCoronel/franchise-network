package co.com.bancolombia.r2dbc.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Table(name = "inventory")
@Getter
@Setter
public class InventoryEntity {

    @Id
    private Long id;
    @Column("branch_id")
    private Long branchId;
    @Column("product_id")
    private Long productId;
    @Column("stock")
    private int stock;

}
