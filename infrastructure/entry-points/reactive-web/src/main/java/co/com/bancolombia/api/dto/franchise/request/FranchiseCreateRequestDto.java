package co.com.bancolombia.api.dto.franchise.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FranchiseCreateRequestDto {

    @NotBlank(message = "The name is required")
    private String name;


}
