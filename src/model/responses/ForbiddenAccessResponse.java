package model.responses;

import model.Resource;

import java.io.File;
import java.io.IOException;

public class ForbiddenAccessResponse extends Response {
    public ForbiddenAccessResponse(Resource resource) throws IOException {
        super.setHttpStatusCode(403);
        super.setHttpStatus("Forbidden");
        super.getHeadersMap().put("Content-Type", resource.getMimeTypes().lookup("html"));
        super.setResponseBody(super.fetchFileData(new File("./resources/Forbidden.html")));
    }
}
