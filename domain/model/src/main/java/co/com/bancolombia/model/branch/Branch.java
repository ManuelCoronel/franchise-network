package co.com.bancolombia.model.branch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Branch {

    private Long id;
    private Long franchiseId;
    private String name;

}
