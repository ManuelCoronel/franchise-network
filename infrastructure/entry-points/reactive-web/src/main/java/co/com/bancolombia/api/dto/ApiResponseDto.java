package co.com.bancolombia.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private String code;
    private LocalDateTime timestamp;

    public ApiResponseDto(boolean success, String message, T data, Object errors,String code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
        this.code = code;
    }


    public static <T> ApiResponseDto<T> ok(T data) {
        return new ApiResponseDto<>(true, "Successful operation", data, null,null);
    }

    public static <T> ApiResponseDto<T> error(String message, Object errors,String code) {
        return new ApiResponseDto<>(false, message, null, errors,code);
    }

}