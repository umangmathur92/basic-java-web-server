package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class HtPassword {

    private HashMap<String, String> authorizedAccountsMap = new LinkedHashMap<>();

    public HtPassword(HtAccess htAccess) throws IOException {
        String authUserFilePath = htAccess.getAuthUserFile();
        parseAndLoad(authUserFilePath);
    }

    private void parseAndLoad(String strFilePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(strFilePath)));
        String[] contentLinesArray = content.split("\n");
        for (String individualLineStr : contentLinesArray) {
            String[] tokens = individualLineStr.split(":");
            if (tokens.length > 0) {
                authorizedAccountsMap.put(tokens[0].trim(), tokens[1].replaceAll(Pattern.quote("{SHA}"), "").trim());
            }
        }
    }

    public HashMap<String, String> getAuthorizedAccountsMap() {
        return authorizedAccountsMap;
    }

}