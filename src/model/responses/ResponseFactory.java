package model.responses;

import model.Request;
import model.Resource;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static model.Request.*;

public class ResponseFactory {

    public static Response getResponse(Request request, Resource resource) throws Exception {
        String verb = request.getVerb();
        switch (verb) {
            case TYPE_GET:
                boolean resourceExists = resource.isExist();
                if (!resourceExists) {
                    return new NotFoundResponse(resource);
                }
                boolean containsFileModifDateHeader = request.getHeadersMap().containsKey("If-Modified-Since");
                boolean isResourceModified = containsFileModifDateHeader && resource.isModifiedSince(request);
                return isResourceModified ? new OkResponse(resource, true) : new NotModifiedResponse();
            case TYPE_PUT:
                boolean resFileCreated = resource.createFile(request);
                return resFileCreated ? new CreatedResponse(resource) : new InternalServerErrorResponse();
            case TYPE_POST:
                return new OkResponse(resource, true);
            case TYPE_DELETE:
                boolean rescExists = resource.isExist();
                if (!rescExists) {
                    return new NotFoundResponse(resource);
                }
                boolean deleteFileSuccessfully = resource.deleteFile(request);
                return deleteFileSuccessfully? new NoContentResponse() : new NotFoundResponse(resource);
            case TYPE_HEAD:
                boolean resExists = resource.isExist();
                if (!resExists) {
                    return new NotFoundResponse(resource);
                }
                boolean hasFileModifDateHeader = request.getHeadersMap().containsKey("If-Modified-Since");
                boolean isResModified = hasFileModifDateHeader && resource.isModifiedSince(request);
                return isResModified ? new OkResponse(resource, false) : new NotModifiedResponse();
            default:
                return new BadRequestResponse();
        }
    }

    private static String executeScript(Resource resource, Request request) throws InterruptedException,IOException {
        List<String> args = getScriptArgs(resource,request);
        ProcessBuilder pb=new ProcessBuilder(args);
        setEnvVariables(request, pb);
        Process process = pb.start();
        return output(process.getInputStream());
    }

    private static void setEnvVariables(Request request,ProcessBuilder procBuilder) throws IOException{
        Map<String,String> envMap = procBuilder.environment();
        for (String currentHeader : request.getHeadersMap().keySet()) {
            envMap.put("HTTP_" + currentHeader.toUpperCase(), request.getHeadersMap().get(currentHeader));
        }
        if(!request.getVerb().equals("POST") && request.getQueryString()!= null) {
            envMap.put("QUERY_STRING", request.getQueryString());
        }
        else if(request.getBody()!= null) {
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
        if((str = br.readLine()) != null){
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
