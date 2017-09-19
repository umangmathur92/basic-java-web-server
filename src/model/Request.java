package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Request {

    public static final String TYPE_GET = "GET";
    public static final String TYPE_PUT = "PUT";
    public static final String TYPE_POST = "POST";
    public static final String TYPE_DELETE = "DELETE";
    public static final String TYPE_HEAD = "HEAD";

    private String uri;
    private String verb;
    private String httpVersion;
    private HashMap<String, String> headersMap = new LinkedHashMap<>();
    private byte[] body;

    public Request(String uri, String verb, String httpVersion, HashMap<String, String> headersMap, byte[] body) {
        this.uri = uri;
        this.verb = verb;
        this.httpVersion = httpVersion;
        this.headersMap = headersMap;
        this.body = body;
    }

/*
    public Request(String completeRequestPacketStr) {
        parseRequestStr(completeRequestPacketStr);
    }

    private void parseRequestStr(String completeRequestPacketStr) {
        String[] strings = completeRequestPacketStr.split("\n");
        for (int i = 0; i < strings.length; i++){
            String strLine = strings[i];
            String[] stringArr;
            if (i==0) {
                stringArr = strLine.split(" ");
                this.verb = stringArr[0];
                this.uri = stringArr[1];
                this.httpVersion = stringArr[2];
            } else if (strLine.contains(":")){
                stringArr = strLine.split(":");
                String key = stringArr[0].trim();
                String value = stringArr[1].trim();
                this.headersMap.put(key, value);
            }
        }
        if (headersMap.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headersMap.get("Content-Length"));
            int completeReqPacketLength = completeRequestPacketStr.length();

            String bodyStr = completeRequestPacketStr.substring(completeReqPacketLength - contentLength);


        }
    }
*/

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HashMap<String, String> getHeadersMap() {
        return headersMap;
    }

    public void setHeadersMap(HashMap<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    private void parse() {

    }

    @Override
    public String toString() {
        return "Request{" +
                "uri='" + uri + '\'' +
                ", verb='" + verb + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", headersMap=" + headersMap +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
