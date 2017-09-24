package model.responses;

import model.Resource;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CreatedResponse extends Response {

    public CreatedResponse(Resource resource) {
        super.setHttpStatusCode(201);
        super.setHttpStatus("Created");
        HashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Location", resource.getModifiedUri());
        super.setHeadersMap(headers);
    }

}