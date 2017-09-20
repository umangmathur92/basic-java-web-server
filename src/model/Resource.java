package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Resource {

    private String modifiedUri;
    private String uri;
    private HttpdConf httpdConf;
    private HtAccess htAccess;
    private MimeTypes mimeTypes;

    public String getModifiedUri() {
        return modifiedUri;
    }
    public void setModifiedUri(String modifiedUri) {
        this.modifiedUri = modifiedUri;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public HttpdConf getHttpdConf() {
        return httpdConf;
    }
    public void setHttpdConf(HttpdConf httpdConf) {
        this.httpdConf = httpdConf;
    }
    public HtAccess getHtAccess() {
        return htAccess;
    }
    public void setHtAccess(HtAccess htAccess) {
        this.htAccess = htAccess;
    }

    public MimeTypes getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(MimeTypes mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public Resource(String uri, HttpdConf httpdConf, MimeTypes mimeTypes) {
        this.httpdConf = httpdConf;
        this.uri = uri;
        this.mimeTypes =  mimeTypes;
        handleResourceUri(httpdConf, uri);
    }

    private void handleResourceUri(HttpdConf config, String uri) {
        modifiedUri = uri;
        //Check for Alias
        HashMap<String, String> aliasesMap = config.getAliasesMap();
        for (String aliasKey : aliasesMap.keySet()) {
            if (uri.contains(aliasKey)) {
                String actualPath = aliasesMap.get(aliasKey);
                modifiedUri = uri.replace(aliasKey, actualPath);
            }
        }
        //Check for Script Alias
        HashMap<String, String> scriptAliasesMap = config.getScriptAliasesMap();
        for (String scriptAliasKey : scriptAliasesMap.keySet()) {
            if (uri.contains(scriptAliasKey)) {
                String actualScriptPath = scriptAliasesMap.get(scriptAliasKey);
                modifiedUri = uri.replace(scriptAliasKey, actualScriptPath);
            }
        }
        //append doc root path
        if(!modifiedUri.contains(config.getDocumentRoot())) {
            modifiedUri = config.getDocumentRoot()+ modifiedUri;
        }
        //check if file, else append directory index
        if (File.separator.equals(modifiedUri.substring(modifiedUri.length()-1))) {
            modifiedUri = modifiedUri + "index.html";
        }
    }

    public boolean isAuthorized(Request request){
        return request.getHeadersMap().containsKey("Authorization");
    }

    public boolean isProtected() throws IOException{
        String parentPath = new File(modifiedUri).getParent();
        File temp = null;
        while ((temp = new File(parentPath)).getParent()!= null) {
            if(new File(temp.getAbsolutePath() + File.separator+".htAccess").exists()){
                htAccess = new HtAccess();
                htAccess.parse(temp.getAbsolutePath());
                return true;
            }
            parentPath = temp.getParent();
        }
        return false;
    }

    public boolean isModified(Request request) throws ParseException {
        String modifiedSinceHeader = request.getHeadersMap().get("If-Modified-Since");
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        Date modifiedSinceDate = (Date)formatter.parse(modifiedSinceHeader);
        File resource = new File(modifiedUri);
        Date lastModified = new Date(resource.lastModified());
        Calendar calender = Calendar.getInstance();
        calender.setTime(modifiedSinceDate);
        calender.set(Calendar.MILLISECOND, 0);
        lastModified = calender.getTime();
        return lastModified.after(modifiedSinceDate);
    }

    public void createFile(Request request) throws IOException {
        File file = new File(modifiedUri);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(modifiedUri);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        bufferedWriter.write(new String(request.getBody()));
        bufferedWriter.close();
    }

    public boolean deleteFile(Request request) {
        File file = new File(modifiedUri);
        if(file.isDirectory()) {
            return false;
        }
        return file.delete();
    }

    public boolean isScript(){
        for (String currentKey : httpdConf.getScriptAliasesMap().keySet()) {
            if (uri.contains(currentKey)) {
                return true;
            }
        }
        return false;
    }

}