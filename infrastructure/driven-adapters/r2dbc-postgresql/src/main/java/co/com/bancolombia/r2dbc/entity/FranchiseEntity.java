package co.com.bancolombia.r2dbc.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("franchise")
@Getter
@Setter
public class FranchiseEntity {

    @Id
    private Long id;
    private String name;

}
