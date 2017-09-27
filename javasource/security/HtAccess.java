package javasource.security;

import javasource.resources.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtAccess {

  private String authUserFile;
  private String authType;
  private String authName;
  private String require;

  public HtAccess(Resource resource) throws IOException {
    File resDirectory = new File(resource.getModifiedUri()).getParentFile();
    String htAccessFilePath = resDirectory.getAbsolutePath() + File.separator + resource.getHttpdConf().getAccessFileName();
    parseAndLoad(htAccessFilePath);
  }

  private void parseAndLoad(String strFilePath) throws IOException {
    String content = new String(Files.readAllBytes(Paths.get(strFilePath)));
    String[] contentLinesArray = content.split("\n");
    for (String individualLineStr : contentLinesArray) {
      String[] tokens = individualLineStr.split("\\s+");
      if (tokens.length > 0) {
        switch (tokens[0].trim()) {
          case "AuthUserFile":
            this.authUserFile = remQuotesFromStr(tokens[1]);
            break;
          case "AuthType":
            this.authType = tokens[1].trim();
            break;
          case "AuthName":
            this.authName = remQuotesFromStr(tokens[1]);
            break;
          case "Require":
            this.require = tokens[1].trim();
            break;
        }
      }
    }
  }

  public String getAuthUserFile() {
    return authUserFile;
  }

  public void setAuthUserFile(String authUserFile) {
    this.authUserFile = authUserFile;
  }

  public String getAuthType() {
    return authType;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  public String getAuthName() {
    return authName;
  }

  public void setAuthName(String authName) {
    this.authName = authName;
  }

  public String getRequire() {
    return require;
  }

  public void setRequire(String require) {
    this.require = require;
  }

  public String getWWWAuthHeaderValue() {
    return authType + " realm=" + authName;
  }

  private String remQuotesFromStr(String str) {
    return str.replaceAll("\"", "").trim();
  }

}