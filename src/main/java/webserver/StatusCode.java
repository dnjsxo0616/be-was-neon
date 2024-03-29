package webserver;

public enum StatusCode {
    OK_200("200 OK"),

    REDIRECTION_302("302 FOUND"),

    GENERATE_NOT_FOUNT_404("404 NOT FOUND"),

    INTERNAL_SERVER_ERROR_500("500 Internal Server Error");

    private final String message;

    StatusCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
