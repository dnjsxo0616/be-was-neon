package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private  final List<String> headers;
    private final byte[] body;


    public HttpResponse(List<String> headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    public HttpResponse(List<String> headers) {
        this.headers = headers;
        this.body = new byte[]{};
    }

    public static HttpResponse staticHttpResponse(File file) {
        return new HttpResponse(addStaticHeader((int) file.length()), setBody(file));
    }

    public static HttpResponse createHttpResponse(String location) {
        return new HttpResponse(addRedirectHeader(location));
    }

    public static HttpResponse contentTypeHttpResponse(File file, String type) {
        return new HttpResponse(addContentTypeHeader((int) file.length(), type), setBody(file));
    }

    private static List<String> addContentTypeHeader(int fileLength, String path) {
        String type = getType(path);
        List<String> headers = new ArrayList<>();
        // 상태라인
        headers.add("HTTP/1.1 200 OK \r\n");
        // 헤더 작성
        headers.add("Content-Type: "+type+";charset=utf-8\r\n");
        headers.add("Content-Length: " + fileLength + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    private static String getType(String path) {
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

    public static List<String> addStaticHeader(int fileLength) {
        List<String> headers = new ArrayList<>();
        // 상태라인
        headers.add("HTTP/1.1 200 OK \r\n");
        // 헤더 작성
        headers.add("Content-Type: text/html;charset=utf-8\r\n");
        headers.add("Content-Length: " + fileLength + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    public static List<String> addRedirectHeader(String location) {
        List<String> headers = new ArrayList<>();
        headers.add( "HTTP/1.1 302 FOUND\r\n");
        // 헤더 작성
        headers.add("Location: " + location + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    public static byte[] setBody(File file)  {
        byte[] body = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return body;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void writeHeader(List<String> headers, DataOutputStream dos) throws IOException {
        for(String line : headers) {
            dos.writeBytes(line);
        }
    }

    public void writeBody(byte[] body, DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

    public void send(OutputStream out){
        DataOutputStream dos = new DataOutputStream(out);
        try {
            writeHeader(getHeaders(), dos);
            writeBody(getBody(), dos);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}