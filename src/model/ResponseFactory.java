package model;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResponseFactory {

    public static Response getResponse(Request request,Resource resource) throws IOException, NoSuchAlgorithmException, ParseException, InterruptedException{

            /*if(resource.isProtected() ){

                //2. Unauthorized access : 401
                if (!resource.isAuthorized(request)){
                    return new UnauthorizedResponse(resource);
                }

                //3. Validation failed : 403
                else if (!resource.getHtacess().validateUser(request)){
                    return new ForbiddenAccessReponse(resource);
                }

            }

            if (request.getVerb()!="PUT") {
                File file=new File(resource.getModifiedUri());
                if(!file.exists()){
                    return new NotFoundResponse(resource);
                }
            }

            if(resource.isScript()){
                return new OkResponseScript(executeScript(resource,request));
            }

            if (((request.getVerb().equals("GET") && request.getHeadersMap().containsKey("If-Modified-Since")) || request.getVerb().equals("HEAD"))
                    && !resource.isModified(request)){

                return new NotModifedResponse();
            }

            if(request.getVerb().equals("PUT")){

                resource.createFile(request);
                return new CreatedResponse(request.getBody().length());
            }

            if(request.getVerb().equals("DELETE")){

                if(resource.deleteFile(request))
                    return new NoContentResponse();
            }
*/
        return new OkResponse(resource);
    }

    private static String executeScript(Resource resource,Request request) throws InterruptedException,IOException {

        List<String> args=new ArrayList<>();

        args=getScriptArgs(resource,request);

        ProcessBuilder pb=new ProcessBuilder(args);
        setEnvVariables(request, pb);
        Process process = pb.start();
        return output(process.getInputStream());
    }

    private static void setEnvVariables(Request request,ProcessBuilder pb) throws IOException{
        Map<String,String> env=pb.environment();
        for (String currentHeader : request.getHeadersMap().keySet()) {
            env.put("HTTP_"+currentHeader.toUpperCase(), request.getHeadersMap().get(currentHeader));
        }
        if(!request.getVerb().equals("POST") && request.getQueryString()!= null) {
            env.put("QUERY_STRING", request.getQueryString());
        }
        else if(request.getBody()!= null) {
            env.put("QUERY_STRING", new String(request.getBody()));
        }
    }

    private static List<String> getScriptArgs(Resource resource, Request request) throws IOException {
        String[] command = readFileType(resource.getModifiedUri());
        List<String> args = Arrays.asList(command);
        args.add(resource.getModifiedUri());
        return args;
    }

    private static String[] readFileType(String modifiedUri) throws IOException {
        File script = new File(modifiedUri);
        String tempString;
        BufferedReader br = new BufferedReader(new FileReader(script));
        if((tempString = br.readLine())!=null){
            return tempString.substring(2).split(" ");
        }
        br.close();
        return null;
    }

    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

}
