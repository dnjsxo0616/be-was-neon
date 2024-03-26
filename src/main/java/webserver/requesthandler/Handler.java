package webserver.requesthandler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.IOException;

public interface Handler {

    HttpResponse getRequest(HttpRequest httpRequest) throws IOException;

    HttpResponse postRequest(HttpRequest httpRequest) throws IOException;
}
