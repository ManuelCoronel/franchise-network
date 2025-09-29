package co.com.bancolombia.api.dto.product.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateNameRequestDto {

    @NotBlank(message = "The name is required")
    private String name;

    @NotNull(message = "The productId is required")
    @Min(value = 1, message = "productId must be a positive number")
    private Long productId;
}
