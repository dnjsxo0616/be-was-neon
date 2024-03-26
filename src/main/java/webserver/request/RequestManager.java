package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RequestManager {

    public static final String CONTENT_LENGTH = "Content-Length";

    private final static Logger logger = LoggerFactory.getLogger(RequestManager.class);

    // http 요청 파싱
    public HttpRequest parseInputStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        String[] tokens = line.split(" ");
        String httpMethod = tokens[0];
        String requestTarget = tokens[1];
        Map<String, String> headers = readHeaders(br);

        String body = readBody(br, headers);


        return new HttpRequest(httpMethod, requestTarget, headers, body);
    }

    // body 생성
    private String readBody(BufferedReader br, Map<String, String> headers) throws IOException {
        if (headers.containsKey(CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH).trim());
            StringBuilder body = new StringBuilder();
            char[] buffer = new char[1024];
            int totalRead = 0;
            while (totalRead < contentLength) {
                int bytesRead = br.read(buffer, 0, Math.min(contentLength - totalRead, buffer.length));
                if (bytesRead == -1) {
                    break;
                }
                body.append(buffer, 0, bytesRead);
                totalRead += bytesRead;
            }
            return body.toString();
        }
        return "";
    }

    // headers 생성
    private Map<String, String> readHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            String[] headerPart = line.split(":");
            headers.put(headerPart[0], headerPart[1]);
        }
        return headers;
    }

}
