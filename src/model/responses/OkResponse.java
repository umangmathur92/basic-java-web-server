package model.responses;

import model.Resource;

import java.io.File;
import java.io.IOException;

public class OkResponse extends Response {

    public OkResponse(Resource resource, boolean shouldSendRespBody) throws IOException {
        super.setHttpStatusCode(200);
        super.setHttpStatus("OK");
        String extension = resource.getModifiedUri().substring(resource.getModifiedUri().lastIndexOf(".")+1);
        super.getHeadersMap().put("Content-Type", resource.getMimeTypes().lookup(extension));
        if (shouldSendRespBody) {
            super.setResponseBody(super.fetchFileMetaData(new File(resource.getModifiedUri())));
        }
    }

}