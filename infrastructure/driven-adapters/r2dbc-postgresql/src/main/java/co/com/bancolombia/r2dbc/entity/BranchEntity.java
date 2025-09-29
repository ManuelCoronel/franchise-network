package co.com.bancolombia.r2dbc.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("branch")
@Getter
@Setter
public class BranchEntity {
    @Id
    private Long id;
    private String name;
    @Column(name = "franchise_id")
    private Long franchiseId;
}
