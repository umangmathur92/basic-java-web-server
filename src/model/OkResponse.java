package model;

import java.io.File;
import java.io.IOException;

public class OkResponse extends Response {

    public OkResponse(Resource resource) throws IOException {

        super.setCode(200);
        super.setReasonPhrase("OK");

        String extension = resource.getModifiedUri().substring(resource.getModifiedUri().lastIndexOf(".")+1);
        super.getHeaders().put("Content-Type", resource.getMimeTypes().lookup(extension));

        super.setBody(super.fetchFileContents(new File(resource.getModifiedUri())));
    }

}
