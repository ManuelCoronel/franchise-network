package co.com.bancolombia.model.constants;

public final class ErrorCodes {


    private ErrorCodes() {
        throw new IllegalStateException("Utility class");
    }

    public static final String FRANCHISE_NOT_FOUND = "001";
    public static final String BRANCH_NOT_FOUND = "002";
    public static final String PRODUCT_NOT_FOUND = "003";
    public static final String INVENTORY_NOT_FOUND = "004";
    public static final String INVENTORY_ALREADY_EXITS = "005";
    public static final String GENERIC_SERVER_ERROR = "SYS_500";
}
