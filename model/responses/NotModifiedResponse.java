package model.responses;

import model.Resource;

import java.io.File;
import java.io.IOException;

public class NotModifiedResponse extends Response {

    public NotModifiedResponse(Resource resource) throws IOException {
        super.setHttpStatusCode(304);
        super.setHttpStatus("Not Modified");
        String extension = resource.getModifiedUri().substring(resource.getModifiedUri().lastIndexOf(".")+1);
        super.getHeadersMap().put("Content-Type", resource.getMimeTypes().lookup(extension));
        byte[] respBytes = super.fetchFileData(new File(resource.getModifiedUri()));
        super.getHeadersMap().put("Content-Length", respBytes.length + "");
    }

}