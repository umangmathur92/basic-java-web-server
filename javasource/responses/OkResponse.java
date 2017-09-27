package javasource.responses;

import javasource.resources.Resource;

import java.io.File;
import java.io.IOException;

public class OkResponse extends Response {

  public OkResponse(Resource resource, boolean shouldSendRespBody) throws IOException {
    super.setHttpStatusCode(200);
    super.setHttpStatus("OK");
    String extension = resource.getModifiedUri().substring(resource.getModifiedUri().lastIndexOf(".") + 1);
    super.getHeadersMap().put("Content-Type", resource.getMimeTypes().lookup(extension));
    byte[] respBytes = super.fetchFileData(new File(resource.getModifiedUri()));
    super.getHeadersMap().put("Content-Length", respBytes.length + "");
    if (shouldSendRespBody) {
      super.setResponseBody(respBytes);
    }
  }

}