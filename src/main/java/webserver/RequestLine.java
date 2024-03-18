package webserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestLine {
    private final String httpMethod;
    private final String path;
    private final String protocol;
    private final Map<String, String> params = new HashMap<>();

    public RequestLine(String line) {
        String[] parseLine = line.split(" ");
        System.out.println("parseLine = " + Arrays.toString(parseLine));
        this.httpMethod = parseLine[0];
        this.path = parseLine[1];
        this.protocol = parseLine[2];

        if (path.contains("?")) {
            String[] pathPart = path.split("\\?");
            if (pathPart.length == 2) {
                parseParams(pathPart);
            }
        }
    }

    private void parseParams(String[] pathPart) {
        String[] paramArray = pathPart[1].split("&");
        for (String param : paramArray) {
            String[] paramKeyValue = param.split("=");
            if (paramKeyValue.length == 2) {
                System.out.println("paramKeyValue1 = " + paramKeyValue[0]);
                System.out.println("paramKeyValue2 = " + paramKeyValue[1]);
                params.put(paramKeyValue[0], paramKeyValue[1]);
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

    public String getPath() {
        return path;
    }

    public String getStartLine() {
        return httpMethod+ " " + path + " " + protocol;
    }
}
