package co.com.bancolombia.api.dto.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequestDto {

    @NotBlank(message = "The name is required")
    private String name;

}
