package model.responses;

import model.Resource;

import java.io.File;
import java.io.IOException;

public class NotFoundResponse extends Response {

    public NotFoundResponse(Resource resource) throws IOException {
        super.setHttpStatusCode(404);
        super.setHttpStatus("Not Found");
        super.getHeadersMap().put("Content-Type:",resource.getMimeTypes().lookup("html"));
        super.setResponseBody(super.fetchFileMetaData(new File("./resources/NotFound.html")));
    }

}