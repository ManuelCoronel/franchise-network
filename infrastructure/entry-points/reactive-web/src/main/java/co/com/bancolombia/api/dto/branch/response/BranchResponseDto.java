package co.com.bancolombia.api.dto.branch.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponseDto {
    private Long id;
    private Long franchiseId;
    private String name;
}
