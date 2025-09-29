package co.com.bancolombia.api.error.handler;


import co.com.bancolombia.api.dto.ApiResponseDto;
import co.com.bancolombia.model.exception.ResourceAlreadyExistsException;
import co.com.bancolombia.model.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ApiResponseDto<String>>> handleConstraintViolationException(
            ConstraintViolationException ex
    ) {
        log.error("Response status exception: 400 with reason {}",ex.getMessage());
        String formattedErrors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                ))
                .toString();

        ApiResponseDto<String> errorResponse = ApiResponseDto.error(
                "Input data validation errors",
                formattedErrors,null
        );

        return Mono.just(
                new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponseDto<String>>> handleGenericException(Exception ex) {
        log.error("Response status exception: 500 with reason {}",ex.getMessage());
        ApiResponseDto<String> errorResponse = ApiResponseDto.error(
                "An internal server error occurred", null,null);

        return Mono.just(
                new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }
    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ApiResponseDto<String>>> handleResponseStatusException(ResponseStatusException ex) {
        log.error("Response status exception: {} with reason {}", ex.getStatusCode(), ex.getReason());
        ApiResponseDto<String> errorResponse = ApiResponseDto.error("Request error",null,null);
        return Mono.just(new ResponseEntity<>(errorResponse, ex.getStatusCode()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ApiResponseDto<String>>> handleNotResourceFoundException(ResourceNotFoundException ex) {
        log.error("Response status exception: 404 with reason {}", ex.getMessage());
        ApiResponseDto<String> errorResponse = ApiResponseDto.error(ex.getMessage(),null,ex.getCode());
        return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public Mono<ResponseEntity<ApiResponseDto<String>>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.error("Response status exception: 422 with reason {}", ex.getMessage());
        ApiResponseDto<String> errorResponse = ApiResponseDto.error(ex.getMessage(),null,ex.getCode());
        return Mono.just(
                new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY)
        );
    }

}