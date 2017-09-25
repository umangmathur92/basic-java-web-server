package model.responses;

import model.HtAccess;
import model.HtPassword;
import model.Request;
import model.Resource;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static model.Request.*;

public class ResponseFactory {

    public static Response getResponse(Request request, Resource resource) throws Exception {
        String verb = request.getVerb();
        if (resource.isProtected()) {
            File resDirectory = new File(resource.getModifiedUri()).getParentFile();
            HtAccess htAccess = new HtAccess(resDirectory.getAbsolutePath() + File.separator + resource.getHttpdConf().getAccessFileName());
            resource.setHtAccess(htAccess);
            String strAuthUserFilePath = htAccess.getAuthUserFile();
            HtPassword htPassword = new HtPassword(strAuthUserFilePath);

            if (request.getHeadersMap().containsKey("Authorization")) {
                String authorization = request.getHeadersMap().get("Authorization");
                String credentials = new String(Base64.getDecoder().decode(authorization.split(" ")[1]), Charset.forName( "UTF-8" ));
                String[] credentialsTokens = credentials.split(":");
                String usrName = credentialsTokens[0];
                String usrPassword = credentialsTokens[1];
                HashMap<String, String> authorizedAccountsMap = htPassword.getAuthorizedAccountsMap();
                boolean userAccountValidated = false;
                if (authorizedAccountsMap.containsKey(usrName)) {
                    userAccountValidated = authorizedAccountsMap.get(usrName).equals(usrPassword);
                }
                return userAccountValidated ? new OkResponse(resource, true): new UnauthorizedResponse(resource);
            } else {
                return new ForbiddenAccessReponse(resource);
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

    private static String executeScript(Resource resource, Request request) throws InterruptedException, IOException {
        List<String> args = getScriptArgs(resource, request);
        ProcessBuilder pb = new ProcessBuilder(args);
        setEnvVariables(request, pb);
        Process process = pb.start();
        return output(process.getInputStream());
    }

    private static void setEnvVariables(Request request, ProcessBuilder procBuilder) throws IOException {
        Map<String, String> envMap = procBuilder.environment();
        for (String currentHeader : request.getHeadersMap().keySet()) {
            envMap.put("HTTP_" + currentHeader.toUpperCase(), request.getHeadersMap().get(currentHeader));
        }
        if (!request.getVerb().equals("POST") && request.getQueryString() != null) {
            envMap.put("QUERY_STRING", request.getQueryString());
        } else if (request.getBody() != null) {
            envMap.put("QUERY_STRING", new String(request.getBody()));
        }
    }

    private static List<String> getScriptArgs(Resource resource, Request request) throws IOException {
        String[] command = readFileType(resource.getModifiedUri());
        List<String> args = Arrays.asList(command);
        args.add(resource.getModifiedUri());
        return args;
    }

    private static String[] readFileType(String modifiedUri) throws IOException {
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
