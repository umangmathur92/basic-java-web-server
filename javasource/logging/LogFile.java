package javasource.logging;

import javasource.request.Request;
import javasource.responses.Response;
import javasource.utilities.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class LogFile {

  private String filePath;

  public LogFile(String filePath) {
    this.filePath = filePath;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public synchronized void write(Request request, Response response, Socket client) throws IOException {
    File logFile = new File(filePath);
    if (!logFile.exists()) {
      logFile.createNewFile();
    }
    String clientIP = client.getInetAddress().toString().replace("/", "");
    String clientName = client.getInetAddress().getHostName();
    SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy:HH:mm:ss Z");
    String dateStr = formatter.format(new Date());
    String bytesReturned = "-";
    Map<String, String> headersMap = response.getHeadersMap();
    if (headersMap != null && headersMap.containsKey("Content-Length")) {
      bytesReturned = headersMap.get("Content-Length");
    }
    String logString = String.format("%s - %s [%s] \"%s %s %s\" %d %s\n",
        clientIP, clientName, dateStr, request.getVerb(), request.getUri(), request.getHttpVersion(), response.getHttpStatusCode(), bytesReturned);
    FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(logString);
    Util.print(logString + '\n');
    bw.close();
  }

}