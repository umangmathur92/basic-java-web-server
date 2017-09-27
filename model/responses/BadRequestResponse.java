package model.responses;

import java.io.File;
import java.io.IOException;

public class BadRequestResponse extends Response {

    public BadRequestResponse() throws IOException {
        super.setHttpStatusCode(400);
        super.setHttpStatus("Bad Request");
        super.getHeadersMap().put("Content-Type","text/html");
        super.setResponseBody(super.fetchFileData(new File("./resources/BadRequest.html")));
    }

}