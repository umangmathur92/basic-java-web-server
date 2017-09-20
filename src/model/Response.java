package model;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class Response {

    private int code;
    private String reasonPhrase;
    private Map<String,String> headers = new LinkedHashMap<>();
    private byte[] body;

    public byte[] getBody() {
        return body;
    }
    public void setBody(byte[] body) {
        this.body = body;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getReasonPhrase() {
        return reasonPhrase;
    }
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void sendResponse(Socket socket) throws IOException{
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1").append(" ").append(code).append(" ").append(reasonPhrase).append("\r\n");
        headers.put("Date", getFormattedDate(new Date()));
        headers.put("Server", "My Server");
        headers.put("Connection", "Closed");
        if(headers != null){
            response.append(parseHeaders());
        }
        response.append("\r\n");
        byte[] responseByteArray = response.toString().getBytes();
        if(body!=null){
            byte[] responseWithBodyByteArray = new byte[responseByteArray.length + body.length];
            System.arraycopy(responseByteArray, 0, responseWithBodyByteArray, 0, responseByteArray.length);
            System.arraycopy(body, 0, responseWithBodyByteArray, responseByteArray.length, body.length);
            socket.getOutputStream().write(responseWithBodyByteArray);
        }
        else{
            socket.getOutputStream().write(responseByteArray);
        }
    }

    public byte[] fetchFileContents(File resourceFile) throws IOException{
        Path filePath = Paths.get(resourceFile.getAbsolutePath());
        byte[] fileContents = Files.readAllBytes(filePath);
        headers.put("Content-Length", fileContents.length+"");
        headers.put("Last-Modified", getFormattedDate(new Date(resourceFile.lastModified())));
        headers.put("Cache-Control", "max-age="+2000);
        return fileContents;
    }

    private StringBuilder parseHeaders (){
        StringBuilder headerStream = new StringBuilder();
        Set<Entry<String, String>> entries = headers.entrySet();
        Iterator<Entry<String, String>> it = entries.iterator();
        while(it.hasNext()) {
            Entry<String, String> e = it.next();
            headerStream.append(e.getKey()).append(": ").append(e.getValue()).append(("\r\n"));
        }
        return headerStream;
    }

    private String getFormattedDate(Date date){
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        return formatter.format(date);
    }

}
