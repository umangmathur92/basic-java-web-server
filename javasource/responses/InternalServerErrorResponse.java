package javasource.responses;

import java.io.File;
import java.io.IOException;

public class InternalServerErrorResponse extends Response {

  public InternalServerErrorResponse() throws IOException {
    super.setHttpStatusCode(500);
    super.setHttpStatus("Internal Server Error");
    super.setResponseBody(fetchFileData(new File("./resources/InternalError.html")));
  }

}