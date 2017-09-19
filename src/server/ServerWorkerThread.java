package server;

import model.Request;
import utilities.Util;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;

class ServerWorkerThread extends Thread {

    private Socket socket;

    public ServerWorkerThread(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);//OutputStream

            InputStream inputStream = socket.getInputStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);

            String firstLine = readRawLineToStr(in);
            String[] variables = firstLine.split(" ");
            String verb = variables[0];
            String uri = variables[1];
            String httpVersion = variables[2];
            HashMap<String, String> headersMap = new LinkedHashMap<>();

            String str = readRawLineToStr(in);
            Util.print(str);
            while (str.length()>0) {
                String[] keyValStrArr = str.split(":");
                String key = keyValStrArr[0].trim();
                String value = keyValStrArr[1].trim();
                headersMap.put(key, value);
                str = readRawLineToStr(in);
            }
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            String contentLength = null;
            if((contentLength = headersMap.get("Content-Length"))!=null) {
                for (int i=0;i<Integer.parseInt(contentLength);i++) {
                    buf.write(in.read());
                }
            }
            byte[] body = buf.toByteArray();
            Request request = new Request(uri, verb, httpVersion, headersMap, body);
            Util.print(request.toString());
            outputPrintWriter.println("HTTP/1.1 200 Success\r\n" + "Content-type: text/html\r\n\r\n" + "<html><head></head><body>This is the HTML body</body></html>\n");
            outputPrintWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte[] readRawLine(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputStream.read()) >= 0) {
            buf.write(ch);
            if (ch == '\n') {
                break;
            }
        }
        if (buf.size() == 0) {
            return null;
        }
        return buf.toByteArray();
    }

    public static String readRawLineToStr(InputStream inputStream) throws IOException {
        byte[] bytes = readRawLine(inputStream);
        return new String(bytes, StandardCharsets.UTF_8).replaceAll("\\r","").replaceAll("\\n","");
    }

}