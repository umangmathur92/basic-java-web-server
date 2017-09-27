package javasource.responses;

import javasource.resources.Resource;
import javasource.utilities.Util;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Response {

    private int httpStatusCode;
    private String httpStatus;
    private HashMap<String,String> headersMap = new LinkedHashMap<>();
    private byte[] responseBody;

    public byte[] getResponseBody() {
        return responseBody;
    }
    public void setResponseBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
    public String getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }
    public Map<String, String> getHeadersMap() {
        return headersMap;
    }
    public void setHeadersMap(HashMap<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public void sendResponse(Socket socket, Resource resource) throws IOException{
        StringBuilder responseStr = new StringBuilder();
        responseStr.append("HTTP/1.1 ").append(httpStatusCode).append(" ").append(httpStatus).append("\r\n");
        headersMap.put("Date", Util.getFormattedDate(new Date()));
        headersMap.put("Server", "web-server-umangmathur_vipulkaranjkar");
        headersMap.put("Connection", "Closed");
        headersMap.put("Cache-Control", "max-age=" + 3600);
        if(headersMap != null) {
            responseStr.append(getHeaderStrFromMap(headersMap));
        }
        responseStr.append("\r\n");
        byte[] respByteArr = responseStr.toString().getBytes();
        if(responseBody != null){
            byte[] respWithBodyByteArr = new byte[respByteArr.length + responseBody.length];
            System.arraycopy(respByteArr, 0, respWithBodyByteArr, 0, respByteArr.length);
            System.arraycopy(responseBody, 0, respWithBodyByteArr, respByteArr.length, responseBody.length);
            socket.getOutputStream().write(respWithBodyByteArr);
        }
        else{
            socket.getOutputStream().write(respByteArr);
        }
    }

    private void SetHeadersFromResFileAttrs(File resFile, byte[] fileContents) {
        headersMap.put("Content-Length", fileContents.length + "");
        headersMap.put("Last-Modified", Util.getFormattedDate(new Date(resFile.lastModified())));
    }

    public byte[] fetchFileData(File resFile) throws IOException{
        Path filePath = Paths.get(resFile.getAbsolutePath());
        byte[] fileContents = Files.readAllBytes(filePath);
        SetHeadersFromResFileAttrs(resFile, fileContents);
        return fileContents;
    }

    private StringBuilder getHeaderStrFromMap(Map<String, String> headersMap){
        StringBuilder headerStr = new StringBuilder();
        for (String headerKey : headersMap.keySet()) {
            String headerVal = headersMap.get(headerKey);
            headerStr.append(headerKey).append(": ").append(headerVal).append("\r\n");
        }
        return headerStr;
    }

}