package model.responses;

import model.Resource;

import java.io.File;
import java.io.IOException;

public class UnauthorizedResponse extends Response {
    public UnauthorizedResponse(Resource resource) throws IOException {
        super.setHttpStatusCode(401);
        super.setHttpStatus("Unauthorized");
        super.getHeadersMap().put("WWW-Authenticate", resource.getHtAccess().createWWWAuthHeader());
        super.getHeadersMap().put("Content-Type", resource.getMimeTypes().lookup("html"));
        super.setResponseBody(super.fetchFileData(new File("./resources/Unauthorized.html")));
    }
}
