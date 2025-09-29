package co.com.bancolombia.model.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    private final String code;

    public ResourceAlreadyExistsException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}