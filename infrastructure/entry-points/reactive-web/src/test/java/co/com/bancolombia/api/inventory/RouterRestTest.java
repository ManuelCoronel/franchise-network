package co.com.bancolombia.api.inventory;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    @Mock
    private InventoryHandler inventoryHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterRest routerRest = new RouterRest();
        RouterFunction<ServerResponse> routerFunction = routerRest.routerInventoryFunction(inventoryHandler);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testCreateInventoryRoute() {

        when(inventoryHandler.createInventory(any()))
                .thenReturn(Mono.just(ServerResponse.status(HttpStatus.CREATED).build().block()));


        webTestClient.post()
                .uri("/api/inventory")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testRemoveInventoryRoute() {

        when(inventoryHandler.removeInventory(any()))
                .thenReturn(Mono.just(ServerResponse.noContent().build().block()));

        webTestClient.delete()
                .uri("/api/inventory/branch/10/product/20")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testUpdateStockFromProductOfBranchRoute() {

        when(inventoryHandler.updateStockFromProductOfBranch(any()))
                .thenReturn(Mono.just(ServerResponse.ok().build().block()));

        webTestClient.patch()
                .uri("/api/inventory")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetTopProductsByFranchiseRoute() {

        when(inventoryHandler.getTopProductsByFranchise(any()))
                .thenReturn(Mono.just(ServerResponse.ok().build().block()));


        webTestClient.get()
                .uri("/api/inventory/top-products/5")
                .exchange()
                .expectStatus().isOk();
    }
}
