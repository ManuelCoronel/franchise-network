package co.com.bancolombia.api.dto.branch.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchUpdateNameRequestDto {

    @NotBlank(message = "The name is required")
    private String name;

    @NotNull(message = "The franchiseId is required")
    @Min(value = 1, message = "branchId must be a positive number")
    private Long branchId;

}
