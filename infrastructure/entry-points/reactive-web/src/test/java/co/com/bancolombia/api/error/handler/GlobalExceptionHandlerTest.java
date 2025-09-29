package co.com.bancolombia.api.error.handler;

import co.com.bancolombia.api.dto.ApiResponseDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleConstraintViolationException() {
        // Arrange
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = Mockito.mock(Path.class);
        when(path.toString()).thenReturn("fieldName");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", violations);

        // Act
        Mono<ResponseEntity<ApiResponseDto<String>>> result = globalExceptionHandler.handleConstraintViolationException(ex);

        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
                    ApiResponseDto<String> body = responseEntity.getBody();
                    assertNotNull(body);
                    assertEquals("Input data validation errors", body.getMessage());
                })
                .verifyComplete();
    }



    @Test
    void handleResponseStatusException_NotFound() {
        // Arrange
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

        // Act
        Mono<ResponseEntity<ApiResponseDto<String>>> result = globalExceptionHandler.handleResponseStatusException(ex);

        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    ApiResponseDto<String> body = responseEntity.getBody();
                    assertNotNull(body);
                    assertEquals("Request error", body.getMessage());
                })
                .verifyComplete();
    }

    @Test
    void handleGenericException() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");

        // Act
        Mono<ResponseEntity<ApiResponseDto<String>>> result = globalExceptionHandler.handleGenericException(ex);

        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
                    ApiResponseDto<String> body = responseEntity.getBody();
                    assertNotNull(body);
                    assertEquals("An internal server error occurred", body.getMessage());
                })
                .verifyComplete();
    }
}
