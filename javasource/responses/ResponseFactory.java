package javasource.responses;

import javasource.request.Request;
import javasource.resources.Resource;
import javasource.security.HtAccess;
import javasource.security.HtPassword;
import javasource.utilities.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static javasource.request.Request.*;

public class ResponseFactory {

  public static Response getResponse(Request request, Resource resource) throws Exception {
    String verb = request.getVerb();
    if (resource.isScript()) {
      return new ScriptOkResponse(execScript(resource, request));
    }
    if (resource.isProtected()) {
      HtAccess htAccess = new HtAccess(resource);
      resource.setHtAccess(htAccess);
      if (request.getHeadersMap().containsKey("Authorization")) {
        boolean hasValidCredentials = checkLoginCredentials(request, resource);
        if (hasValidCredentials) {
          return resource.isExist() ? new OkResponse(resource, true) : new NotFoundResponse(resource);
        } else {
          return new ForbiddenAccessResponse(resource);
        }
      } else {
        return new UnauthorizedResponse(resource);
      }
    }
    switch (verb) {
      case TYPE_GET:
        if (!resource.isExist()) {
          return new NotFoundResponse(resource);
        }
        if (request.getHeadersMap().containsKey("If-Modified-Since")) {
          return resource.isModifiedSince(request) ? new OkResponse(resource, true) : new NotModifiedResponse(resource);
        }
        return new OkResponse(resource, true);
      case TYPE_PUT:
        boolean resFileCreated = resource.createFile(request);
        return resFileCreated ? new CreatedResponse(resource) : new InternalServerErrorResponse();
      case TYPE_POST:
        if (!resource.isExist()) {
          return new NotFoundResponse(resource);
        }
        return new OkResponse(resource, true);
      case TYPE_DELETE:
        if (!resource.isExist()) {
          return new NotFoundResponse(resource);
        }
        boolean deleteFileSuccessfully = resource.deleteFile(request);
        return deleteFileSuccessfully ? new NoContentResponse() : new NotFoundResponse(resource);
      case TYPE_HEAD:
        if (!resource.isExist()) {
          return new NotFoundResponse(resource);
        }
        if (request.getHeadersMap().containsKey("If-Modified-Since")) {
          return resource.isModifiedSince(request) ? new OkResponse(resource, false) : new NotModifiedResponse(resource);
        }
        return new OkResponse(resource, false);
      default:
        return new BadRequestResponse();
    }
  }

  private static boolean checkLoginCredentials(Request request, Resource resource) throws IOException, NoSuchAlgorithmException {
    String authorizationStr = request.getHeadersMap().get("Authorization");
    String[] credentialsTokens = getDecodedInputCredentials(authorizationStr);
    String inputUsrName = credentialsTokens[0].trim();
    String inputUsrPass = Util.convertToSHA1Base64Encoding(credentialsTokens[1].trim());
    HtPassword htPassword = new HtPassword(resource.getHtAccess());
    return compareInputCredentialsWithHtPassword(htPassword, inputUsrName, inputUsrPass);
  }

  private static boolean compareInputCredentialsWithHtPassword(HtPassword htPassword, String inputUsrName, String inputUsrPass) {
    HashMap<String, String> authorizedAccountsMap = htPassword.getAuthorizedAccountsMap();
    if (htPassword.getAuthorizedAccountsMap().containsKey(inputUsrName)) {
      String validPassword = authorizedAccountsMap.get(inputUsrName);
      return inputUsrPass.equals(validPassword);
    }
    return false;
  }

  private static String[] getDecodedInputCredentials(String authorizationStr) {
    String src = authorizationStr.split(" ")[1];
    String decodedCredentialsStr = new String(Base64.getDecoder().decode(src), Charset.forName("UTF-8"));
    return decodedCredentialsStr.split(":");
  }

  private static String execScript(Resource resource, Request request) throws InterruptedException, IOException {
    List<String> scriptArgs = getScriptArgs(resource, request);
    ProcessBuilder processBuilder = new ProcessBuilder(scriptArgs);
    setEnvVariables(request, processBuilder);
    Process process = processBuilder.start();
    return output(process.getInputStream());
  }

  private static void setEnvVariables(Request request, ProcessBuilder procBuilder) throws IOException {
    Map<String, String> envMap = procBuilder.environment();
    for (String currentHeader : request.getHeadersMap().keySet()) {
      envMap.put("HTTP_" + currentHeader.toUpperCase(), request.getHeadersMap().get(currentHeader));
    }
    if (!request.getVerb().equals(TYPE_POST) && request.getQueryString() != null) {
      envMap.put("QUERY_STRING", request.getQueryString());
    } else if (request.getBody() != null) {
      envMap.put("QUERY_STRING", new String(request.getBody()));
    }
  }

  private static List<String> getScriptArgs(Resource resource, Request request) throws IOException {
    String[] str = readScriptFile(resource.getModifiedUri());
    List<String> scriptArgs = new ArrayList<>();
    if (str != null) {
      scriptArgs.addAll(Arrays.asList(str));
    }
    scriptArgs.add(resource.getModifiedUri());
    return scriptArgs;
  }

  private static String[] readScriptFile(String modifiedUri) throws IOException {
    File scriptFile = new File(modifiedUri);
    String str;
    BufferedReader br = new BufferedReader(new FileReader(scriptFile));
    if ((str = br.readLine()) != null) {
      return str.substring(2).split(" ");
    }
    br.close();
    return null;
  }

  private static String output(InputStream inputStream) throws IOException {
    StringBuilder str = new StringBuilder();
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        str.append(line).append(System.getProperty("line.separator"));
      }
    } finally {
      bufferedReader.close();
    }
    return str.toString();
  }

}