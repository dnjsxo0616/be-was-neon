package webserver.response;

import java.util.ArrayList;
import java.util.List;

//200 302 404 코드를 생성해서 보냄

public class HttpResponseBuilder {

    private final List<String> headers = new ArrayList<>();

    public void setStatus(String status) {
        headers.add("HTTP/1.1 " + status + "\r\n");
    }

    public void setContentType(String type) {
        headers.add("Content-Type: " + type + ";charset=utf-8\r\n");
    }

    public void setContentLength(int fileLength) {
        headers.add("Content-Length: " + fileLength + "\r\n");
    }

    public void setNewLine() {
        headers.add("\r\n");
    }

    public void setLocation(String location) {
        headers.add("Location: " + location + "\r\n");
    }

    public void setCookie(String cookieValue) {
        headers.add("Set-Cookie: session=" + cookieValue+ "; Path=/\r\n");
    }

    public void setCookieMaxAge(String cookieValue, String maxAge) {
        headers.add("Set-Cookie: session=" + cookieValue + "; Max-Age=" + maxAge + "; Path=/\r\n");
    }

    public HttpResponse buildResponse(byte[] body) {
        return new HttpResponse(headers, body);
    }

    public HttpResponse buildResponse() {
        return new HttpResponse(headers);
    }

}
