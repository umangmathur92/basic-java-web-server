package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Base64;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class HtPassword {

    private HashMap<String, String> authorizedAccountsMap = new LinkedHashMap<>();

    public HtPassword(String strFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(strFilePath)));
        String[] contentLinesArray = content.split("\n");
        for (String individualLineStr : contentLinesArray) {
            String[] tokens = individualLineStr.split(":");
            authorizedAccountsMap.put(tokens[0].trim(), tokens[1].replaceAll(Pattern.quote("{SHA1}"),"").trim());
        }
        //jrob:{SHA}W6ph5Mm5Pz8GgiULbPgzG37mj9g=
    }

    //    public HtPassword(String filePathStr) throws IOException {
//        Path filePath = Paths.get(filePathStr);
//        System.out.println("Password file: " + filePathStr);
//
//        String loginsConfStr = new String(Files.readAllBytes(filePath));
//        parse(loginsConfStr);
//    }

    public HashMap<String, String> getAuthorizedAccountsMap() {
        return authorizedAccountsMap;
    }

    public void setAuthorizedAccountsMap(HashMap<String, String> Logins) {
        this.authorizedAccountsMap = Logins;
    }

    //        protected void parse(String loginsConfStr) throws IOException {
        public void parse(String loginsConfStr) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(loginsConfStr)));
        String str = null;
        while ((str = br.readLine()) != null) {
            String[] tokens = str.split(":");
            authorizedAccountsMap.put(tokens[0], tokens[1].replace("{SHA}", "").trim());
        }
        br.close();
    }

//    public boolean isAuthorized(String authInfo) {
//        // authInfo is provided in the header received from the client
//        // as a Base64 encoded string.
//        String credentials = new String(
//                Base64.getDecoder().decode(authInfo),
//                Charset.forName("UTF-8")
//        );
//
//        // The string is the key:value pair username:password
//        String[] tokens = credentials.split(":");
//
//        // TODO: implement this
//    }
//
//    private boolean verifyPassword(String username, String password) {
//        // encrypt the password, and compare it to the password stored
//        // in the password file (keyed by username)
//        // TODO: implement this
//    }

    private String encryptClearPassword(String password) {
        // Encrypt the cleartext password (that was decoded from the Base64 String
        // provided by the client) using the SHA-1 encryption algorithm
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            return "";
        }
    }
}