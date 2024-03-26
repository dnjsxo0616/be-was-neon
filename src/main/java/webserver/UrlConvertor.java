package webserver;

import java.io.File;

public class UrlConvertor {
    private final static String BASE_URL = "src/main/resources/static";
    public final static String INDEX_URL = "/index.html";
    public final static String CREATE_URL = "/create";

    public String urlController(String requestTarget) {
        StringBuilder sb = new StringBuilder(BASE_URL);
        sb.append(requestTarget);

        File file = new File(sb.toString());

        if (file.isDirectory()) {
            sb.append(INDEX_URL);
        }
        return sb.toString();
    }

    public String typeController(String requestTarget) {
        return ContentType.getType(requestTarget);
    }
}
