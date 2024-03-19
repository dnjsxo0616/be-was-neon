package webserver;

public enum ContentType {
    CSS("text/css"),
    JS("application/javascript"),
    PNG("image/png"),
    JPG("image/jpeg"),
    ICO("image/x-icon"),
    SVG("image/svg+xml"),
    HTML("text/html");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public static String getType(String path) {
        String type = "text/html";
        if (path.endsWith(".css")) {
            type = "text/css";
        } else if (path.endsWith(".js")) {
            type = "application/javascript";
        } else if (path.endsWith(".png")) {
            type = "image/png";
        } else if (path.endsWith(".jpg")) {
            type = "image/jpeg";
        } else if (path.endsWith(".ico")) {
            type = "image/x-icon";
        } else if (path.endsWith(".svg")) {
            type = "image/svg+xml";
        }
        return type;
    }

    public String getType() {
        return type;
    }
}
