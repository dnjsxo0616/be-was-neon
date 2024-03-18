package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String startLine;
    private final List<String> headerLine = new ArrayList<>();
    private final Map<String, String> params = new HashMap<>();

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line = br.readLine();
        this.startLine = line;
        if (getPath().contains("?")) {
            String[] pathPart = getPath().split("\\?");
            if (pathPart.length == 2) {
                parseParams(pathPart);
            }
        }
        while (!(line = br.readLine()).isEmpty()) {
            this.headerLine.add(line);
        }
    }

    public void printLog() {
        logger.debug("startLine : {}", startLine);
        headerLine.forEach(header -> logger.debug("header : {}", header));
    }

    public String getHttpMethod() {
        String[] startPart = parseStartLine(startLine);
        return startPart[0];
    }

    public String getPath() {
        String[] startPart = parseStartLine(startLine);
        return startPart[1];
    }

    private String[] parseStartLine(String line) {
        return line.split(" ");
    }


    private void parseParams(String[] pathPart) {
        String[] paramArray = pathPart[1].split("&");
        for (String param : paramArray) {
            String[] paramKeyValue = param.split("=");
            if (paramKeyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(paramKeyValue[0], "UTF-8");
                    String value = URLDecoder.decode(paramKeyValue[1], "UTF-8");
                    params.put(key, value);
                } catch (UnsupportedEncodingException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    public String getUserId() {
        return params.get("userId");
    }

    public String getName() {
        return params.get("name");
    }

    public String getEmail() {
        return params.get("email");
    }

    public String getPassword() {
        return params.get("password");
    }


}
