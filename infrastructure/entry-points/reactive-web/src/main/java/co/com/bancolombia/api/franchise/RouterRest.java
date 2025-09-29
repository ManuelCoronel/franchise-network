package co.com.bancolombia.api.franchise;

import co.com.bancolombia.api.branch.BranchHandler;
import co.com.bancolombia.api.dto.franchise.request.FranchiseCreateRequestDto;
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


@Configuration("franchiseRouterRest")
public class RouterRest {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchise",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "createFranchise",
                    operation = @Operation(
                            operationId = "createFranchise",
                            summary = "Create franchise",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = FranchiseCreateRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Franchise created",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    )

                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/franchise",
                    produces = {"application/json"},
                    method = RequestMethod.PUT,
                    beanClass = FranchiseHandler.class,
                    beanMethod = "updateNameFranchise",
                    operation = @Operation(
                            operationId = "updateNameFranchise",
                            summary = "update name franchise",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Franchise update",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApiResponse.class)
                                            )
                                    )
                            }
                    )
            )
     })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler handler) {
        return RouterFunctions.route().
                POST("/api/franchise", handler::createFranchise)
                .PUT("/api/franchise",handler::updateNameFranchise)
                .build();

    }


}
