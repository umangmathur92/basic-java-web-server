package model.responses;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CreatedResponse extends Response {

    public CreatedResponse(int contentLength) {
        super.setHttpStatusCode(201);
        super.setHttpStatus("Created");
        HashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Length", String.valueOf(contentLength));
        super.setHeadersMap(headers);
    }

}