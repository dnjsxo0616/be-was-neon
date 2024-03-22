package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RequestManager {

    private final static Logger logger = LoggerFactory.getLogger(RequestManager.class);

    public HttpRequest parseInputStream(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        String[] tokens = line.split(" ");
        String httpMethod = tokens[0];
        String requestTarget = tokens[1];
        Map<String, String> headers = readHeader(br);

        if (requestTarget.contains("?")) {
            Map<String, String> params = createParams(requestTarget);
            return new HttpRequest(httpMethod, requestTarget, headers, params);
        }
        return new HttpRequest(httpMethod, requestTarget, headers);

    }


    private Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            String[] headerPart = line.split(":");
            headers.put(headerPart[0], headerPart[1]);
        }
        return headers;
    }

    private Map<String, String> createParams(String requestTarget) {
        String[] pathPart = requestTarget.split("\\?");
        return saveParams(pathPart);
    }

    private Map<String, String> saveParams(String[] pathPart) {
        Map<String, String> params = new HashMap<>();
        String[] paramArray = pathPart[1].split("&");
        for (String param : paramArray) {
            String[] paramKeyValue = param.split("=");
            if (paramKeyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(paramKeyValue[0], "UTF-8");
                    String value = URLDecoder.decode(paramKeyValue[1], "UTF-8");
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    logger.error("디코딩 오류: {}", e.getMessage());
                }
            } else {
                logger.warn("잘못된 형식: {}", param);
            }
        }
        return params;
    }
}
