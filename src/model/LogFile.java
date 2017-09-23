package model;

// package Logs;

        import model.Request;
        import model.responses.Response;

        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.net.Socket;
        import java.text.SimpleDateFormat;
        import java.util.Date;

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

        File logFile;
        logFile = new File("/Users/vipulkaranjkar/IdeaProjects/web-server-umangmathur_vipulkaranjkar/logs/log.txt");

        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        String clientIP = client.getInetAddress().toString().replace("/", "");
        String clientName = client.getInetAddress().getHostName();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy:HH:mm:ss Z");
        String logDate = formatter.format(new Date());

        String bytesReturned = null;

        if (response.getHeadersMap() != null && response.getHeadersMap().containsKey("Content-Length")) {
            bytesReturned = response.getHeadersMap().get("Content-Length");
        } else {
            bytesReturned = "-";
        }

        String logString = String.format("%s - %s [%s] \"%s %s %s\" %d %s\n",
                clientIP, clientName, logDate, request.getVerb(), request.getUri(), request.getHttpVersion(), response.getHttpStatusCode(), bytesReturned);

        FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(logString);
        System.out.println(logString + '\n');
        bw.close();
    }
}