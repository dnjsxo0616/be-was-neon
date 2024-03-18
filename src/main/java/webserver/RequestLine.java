package webserver;

import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestLine {
    private final String httpMethod;
    private final String path;
    private final Map<String, String> params = new HashMap<>();

    public RequestLine(String line) {
        String[] strings = line.split(" ");
        this.httpMethod = strings[0];
        this.path = strings[1];

        if(path.contains("?")) {
            String[] pathPart = path.split("\\?");
            if(pathPart.length == 2) {
                parseParams(pathPart);
            }
        }
    }

    private void parseParams(String[] pathPart) {
        String[] paramArray = pathPart[1].split("&");
        for(String param : paramArray) {
            String[] paramKeyValue = param.split("=");
            if(paramKeyValue.length == 2) {
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
}
