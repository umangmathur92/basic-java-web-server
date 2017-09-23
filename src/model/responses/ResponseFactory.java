package model.responses;

import model.Request;
import model.Resource;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResponseFactory {

    public static Response getResponse(Request request, Resource resource) throws IOException, NoSuchAlgorithmException, ParseException, InterruptedException {

        if(request.getVerb().equals("PUT")) {
            resource.createFile(request);
            return new CreatedResponse(request.getBody().length);
        }

        return new OkResponse(resource);
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
