package co.com.bancolombia.api.product;

import co.com.bancolombia.api.branch.BranchHandler;
import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.branch.request.BranchCreateRequestDto;
import co.com.bancolombia.api.dto.branch.request.BranchUpdateNameRequestDto;
import co.com.bancolombia.api.dto.branch.response.BranchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;



@Configuration("productRouterRest")
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/branch",
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = ProductHandler.class,
                    beanMethod = "createBranch",
                    operation = @Operation(
                            operationId = "createBranch",
                            summary = "Create branch",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = BranchCreateRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Branch created",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Resource not Found",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class)
                                            )
                                    )


                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/branch",
                    produces = { "application/json" },
                    method = RequestMethod.PUT,
                    beanClass = ProductHandler.class,
                    beanMethod = "updateNameBranch",
                    operation = @Operation(
                            operationId = "updateProductBranch",
                            summary = "Update product name",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = BranchUpdateNameRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Branch updated",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "Resource not Found",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Bad request",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Internal server error",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponseDto.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerProductFunction(ProductHandler handler) {
        return RouterFunctions.route()
                .POST("/api/product", handler::createProduct)
                .PUT("/api/product", handler::updateNameProduct)
                .build();
    }


}
