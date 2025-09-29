package co.com.bancolombia.api.inventory;

import co.com.bancolombia.api.branch.BranchHandler;
import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.api.dto.branch.request.BranchCreateRequestDto;
import co.com.bancolombia.api.dto.branch.request.BranchUpdateNameRequestDto;
import co.com.bancolombia.api.dto.inventory.request.InventoryCreateRequestDto;
import co.com.bancolombia.api.dto.inventory.request.InventoryUpdateRequestDto;
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



@Configuration("inventoryRouterRest")
public class RouterRest {


    @RouterOperations({
            @RouterOperation(
                    path = "/api/inventory",
                    produces = { "application/json" },
                    method = RequestMethod.POST,
                    beanClass = InventoryHandler.class,
                    beanMethod = "createInventory",
                    operation = @Operation(
                            operationId = "createInventory",
                            summary = "Create inventory",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = InventoryCreateRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Inventory created",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    )

                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/inventory/branch/{branchId}/product/{productId}",
                    produces = { "application/json" },
                    method = RequestMethod.DELETE,
                    beanClass = InventoryHandler.class,
                    beanMethod = "removeInventory",
                    operation = @Operation(
                            operationId = "removeInventory",
                            summary = "Remove inventory",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Remove inventory"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/inventory",
                    produces = { "application/json" },
                    method = RequestMethod.PATCH,
                    beanClass = InventoryHandler.class,
                    beanMethod = "updateStockFromProductOfBranch",
                    operation = @Operation(
                            operationId = "updateStockFromProductOfBranch",
                            summary = "Update stock",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = InventoryUpdateRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Stock updated",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    )

                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/inventory",
                    produces = { "application/json" },
                    method = RequestMethod.GET,
                    beanClass = InventoryHandler.class,
                    beanMethod = "getTopProductsByFranchise",
                    operation = @Operation(
                            operationId = "getTopProductsByFranchise",
                            summary = "Get top of products by franchise",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "List of products with branch by franchise",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    )

                            }
                    )
            )
    })
    @Bean
    public RouterFunction<ServerResponse> routerInventoryFunction(InventoryHandler handler) {
        return RouterFunctions.route()
                .POST("/api/inventory", handler::createInventory)
                .DELETE("/api/inventory/branch/{branchId}/product/{productId}", handler::removeInventory)
                .PATCH("/api/inventory", handler::updateStockFromProductOfBranch)
                .GET("api/inventory/top-products/{franchiseId}",handler::getTopProductsByFranchise)
                .build();
    }


}
