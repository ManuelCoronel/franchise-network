package co.com.bancolombia.api.dto.branch.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchCreateRequestDto{

    @NotNull(message = "The franchise is required")
    private Long franchiseId;
    @NotBlank(message = "The name is required")
    private String name;
}
