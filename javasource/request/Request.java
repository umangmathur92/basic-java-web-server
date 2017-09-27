package javasource.request;

import javasource.utilities.Util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
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
  private String queryString;

  public Request(String uri, String verb, String httpVersion, HashMap<String, String> headersMap, byte[] body) {
    this.uri = uri;
    this.verb = verb;
    this.httpVersion = httpVersion;
    this.headersMap = headersMap;
    this.body = body;
  }

  public Request(Socket socket) {
    try {
      if (socket != null) {
        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
        String firstLine = Util.readRawLineToStr(in);
        Util.print(firstLine);
        String[] variables = firstLine.split(" ");
        this.verb = variables[0];
        this.uri = variables[1];
        this.httpVersion = variables[2];
        if (uri.contains("?")) {
          this.uri = variables[1].substring(0, variables[1].indexOf("?"));
          this.queryString = variables[1].substring(variables[1].indexOf("?") + 1);
        }
        String str = Util.readRawLineToStr(in);
        Util.print(str);
        while (str.length() > 0) {
          String[] keyValStrArr = str.split(": ");
          String key = keyValStrArr[0].trim();
          String value = keyValStrArr[1].trim();
          this.headersMap.put(key, value);
          str = Util.readRawLineToStr(in);
          Util.print(str);
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        String contentLength;
        if ((contentLength = headersMap.get("Content-Length")) != null) {
          for (int i = 0; i < Integer.parseInt(contentLength); i++) {
            buf.write(in.read());
          }
        }
        this.body = buf.toByteArray();
        Util.print(new String(body));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

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

  public String getQueryString() {
    return queryString;
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
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

  public boolean isValid() {
    boolean isValid = false;
    switch (verb) {
      case TYPE_GET:
      case TYPE_PUT:
      case TYPE_POST:
      case TYPE_DELETE:
      case TYPE_HEAD:
        isValid = true;
        break;
    }
    return isValid;
  }

}