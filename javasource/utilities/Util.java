package javasource.utilities;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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

    public static String getFormattedDate(Date date){
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        return formatter.format(date);
    }

    public static boolean createFile(String fileUriStr, byte[] fileContentByteArr) throws IOException {
        File file = new File(fileUriStr);
        if (!file.getParentFile().exists()) {
            boolean parentDirCreated = file.getParentFile().mkdir();
            if (!parentDirCreated) {
                return false;
            }
        }
        if(!file.exists()){
            boolean newFileCreated = file.createNewFile();
            if (!newFileCreated) {
                return false;
            }
        }
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(fileContentByteArr);
        outputStream.close();
        return true;
    }

    public static boolean deleteFile(String modifiedUri) {
        File file = new File(modifiedUri);
        if(file.isDirectory()) {
            return false;
        }
        return file.delete();
    }

    public static String convertToSHA1Base64Encoding(String inputStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(inputStr.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return new String(Base64.getEncoder().encode(digest));
    }

}