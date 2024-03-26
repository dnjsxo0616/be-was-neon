package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private final byte[] body;
    private final List<String> headers;

    public HttpResponse(List<String> headers, byte[] body) {
        this.headers = headers;
        this.body = body;
    }

    public HttpResponse(List<String> headers) {
        this.headers = headers;
        this.body = new byte[]{};
    }

    public void send(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            writeHeader(dos);
            writeBody(dos);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void writeHeader(DataOutputStream dos) throws IOException {
        for (String header : headers) {
            dos.writeBytes(header);
        }
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        dos.write(body, 0, body.length);
    }

}
