package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HtAccess {

    private String authUserFile;
    private String authType;
    private String authName;
    private String require;

    public void setAuthUserFile(String authUserFile) {
        this.authUserFile = authUserFile;
    }
    public void setAuthType(String authType) {
        this.authType = authType;
    }
    public void setAuthName(String authName) {
        this.authName = authName;
    }
    public void setRequire(String require) {
        this.require = require;
    }

    public HtAccess(String strFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(strFilePath)));
        String[] contentLinesArray = content.split("\n");
        for (String individualLineStr : contentLinesArray) {
            String[] tokens = individualLineStr.split("\\s+");
            switch(tokens[0].trim()) {
                case "AuthUserFile":
                    this.authUserFile = remQuotesFromStr(tokens[1]);
                    break;
                case "AuthType":
                    this.authType = tokens[1];
                    break;
                case "AuthName":
                    this.authName = remQuotesFromStr(tokens[1]);
                    break;
                case "Require":
                    this.require = tokens[1];
                    break;
            }
        }

    }

    public String getAuthUserFile() {
        return authUserFile;
    }

    public String getAuthType() {
        return authType;
    }

    public String getAuthName() {
        return authName;
    }

    public String getRequire() {
        return require;
    }

    /*public boolean validateUser(Request request) throws IOException, NoSuchAlgorithmException {
    String[] requestAuthInformation = fetchAuthInformation(request.getHeadersMap().get("Authorization").split(" ")[1]);
        return fetchPasswordAndCompare(requestAuthInformation, htPassword);
    }*/

    public String[] fetchAuthInformation(String authHeader) {
        byte[] base64Decoded = Base64.getDecoder().decode(authHeader);
        return new String(base64Decoded).split(":");
    }

    public boolean fetchPasswordAndCompare(String[] userInfo, HtPassword htpassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String userName = userInfo[0];
        String userPass = userInfo[1];
        String convertedSHA = convertSHAforPassword(userPass);
        String base64DecodedStoredPassword = htpassword.getAuthorizedAccountsMap().get(userName);
        if (base64DecodedStoredPassword != null && base64DecodedStoredPassword.equalsIgnoreCase(convertedSHA)) {
            return true;
        }
        return false;
    }

    public String convertSHAforPassword(String inputPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(inputPassword.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return new String(Base64.getEncoder().encode(digest));
    }

    public String createWWWAuthHeader() {
        return authType + " " + "realm=" + authName;
    }

    private String remQuotesFromStr(String str) {
        return str.replaceAll("\"", "").trim();
    }
}