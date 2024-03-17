package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private final String BASE_URL = "src/main/resources/static";
    private final String requestLine;
    private final List<String> headerLine = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line = br.readLine();
        this.requestLine = line;
        while (!(line = br.readLine()).isEmpty()) {
            this.headerLine.add(line);
        }
    }

    public void logHeaders() {
        logger.debug("requestLine" + requestLine);
        headerLine.forEach(header -> logger.debug("header : {}", header));
    }

    public byte[] staticFileReader() throws IOException {
        String[] tokens = requestLine.split(" ");
        String path = tokens[1];
        String filepath = BASE_URL + path;

        try (FileInputStream fileInputStream = new FileInputStream(filepath)) {
            // 파일을 읽어 바이트 배열로 반환
            return fileInputStream.readAllBytes();
        }
    }
}
