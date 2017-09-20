package utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Util {

    public static void print(String str) {
        System.out.println(str);
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
