package javasource.responses;


public class ScriptOkResponse extends Response {

    public ScriptOkResponse(String scriptResponse) {
        super.setHttpStatusCode(200);
        super.setHttpStatus("OK");
        String[] responseLines = scriptResponse.split(System.getProperty("line.separator"));
        super.getHeadersMap().put("Content-Type", responseLines[0].substring(responseLines[0].indexOf(':') + 1));
        super.getHeadersMap().put("Content-Length", scriptResponse.getBytes().length + "");
        super.setResponseBody(scriptResponse.getBytes());
    }

}

