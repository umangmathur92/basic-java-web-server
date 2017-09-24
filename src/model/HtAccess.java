package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HtAccess {
    private HtPassword authUserFile;
    private String authType;
    private String authName;
    private String require;

    public void setAuthUserFile(HtPassword authUserFile) {
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

//    public HtAccess(HtPassword authUserFile, String authType, String authName, String require) {
//        this.authUserFile = authUserFile;
//        this.authType = authType;
//        this.authName = authName;
//        this.require = require;
//    }

    public HtPassword getAuthUserFile() {
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

    public void parse (String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath+File.separator+".htaccess")));
            String str = null;
            while ((str = br.readLine()) != null) {
                process(str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(String input) throws IOException {
        String[] split = input.split(" ");
        if (split[0].equals("AuthUserFile")) {

//            String fp = new String("/Users/vipulkaranjkar/IdeaProjects/web-server-umangmathur_vipulkaranjkar/public_html/.htaccess");
//            HtPassword htpassword = new HtPassword(fp);

            HtPassword htpassword = new HtPassword();
            htpassword.parse(split[1].split("\"")[1]);
            authUserFile = htpassword;
        }
        if (split[0].equals("AuthType"))
            authType = split[1];

        if (split[0].equals("AuthName")) {
            authName = input.substring(input.indexOf("\""));
        }

        if (split[0].equals("Require"))
            require = split[1];
    }

    public boolean validateUser(Request request) throws IOException, NoSuchAlgorithmException {
    String[] requestAuthInformation = fetchAuthInformation(request.getHeadersMap().get("Authorization").split(" ")[1]);
        return fetchPasswordAndCompare(requestAuthInformation, authUserFile);
    }

    public String[] fetchAuthInformation(String authHeader) {
        byte[] base64Decoded = Base64.getDecoder().decode(authHeader);
        return new String(base64Decoded).split(":");
    }

    public boolean fetchPasswordAndCompare(String[] userInfo, HtPassword htpassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String userName = userInfo[0];
        String userPass = userInfo[1];
        String convertedSHA = convertSHAforPassword(userPass);
        String base64DecodedStoredPassword = htpassword.getLogins().get(userName);
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
}