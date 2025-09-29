package co.com.bancolombia.api.branch;

import co.com.bancolombia.api.dto.branch.request.BranchCreateRequestDto;
import co.com.bancolombia.api.dto.franchise.request.FranchiseCreateRequestDto;
import co.com.bancolombia.api.franchise.FranchiseHandler;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration("branchRouterRest")
public class RouterRest {


    @RouterOperations({
            @RouterOperation(
                    path = "/api/franchise",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = FranchiseHandler.class,
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
                    method = RequestMethod.PATCH,
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
    public RouterFunction<ServerResponse> routerBranchFunction(BranchHandler handler) {
        return RouterFunctions.route().
                POST("/api/branch", handler::createBranch)
                .PATCH("/api/branch",handler::updateNameBranch)
                .build();

    }
}
