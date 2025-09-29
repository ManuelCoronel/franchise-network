package co.com.bancolombia.r2dbc.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "product")
@Getter
@Setter
public class ProductEntity {
    @Id
    private Long id;
    private String name;

}
