package model;

import utilities.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Resource {

    private String modifiedUri;
    private String uri;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private HtAccess htaccess;
    private boolean isScript;

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
    public MimeTypes getMimeTypes() {
        return mimeTypes;
    }
    public void setMimeTypes(MimeTypes mimeTypes) {
        this.mimeTypes = mimeTypes;
    }
    public HtAccess getHtAccess() {
        return htaccess;
    }
    public void setHtAccess(HtAccess htaccess) {
        this.htaccess = htaccess;
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
                isScript = true;
            }
        }
        //append document root path
        if(!modifiedUri.contains(config.getDocumentRoot())) {
            modifiedUri = config.getDocumentRoot() + modifiedUri;
        }
        //check if file, else append directory index
        if (File.separator.equals(modifiedUri.substring(modifiedUri.length()-1))) {
            modifiedUri = modifiedUri + "index.html";
        }
    }

    public boolean isAuthorized(Request request) {
        if (request.getHeadersMap().containsKey("Authorization")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isProtected() throws IOException {
        String parentPath = new File(modifiedUri).getParent();
        File temp = null;
        while ((temp = new File(parentPath)).getParent() != null) {
            if (new File(temp.getAbsolutePath() + File.separator + ".htaccess").exists()) {
                htaccess = new HtAccess();
                htaccess.parse(temp.getAbsolutePath());
                return true;
            }
            parentPath = temp.getParent();
        }
        return false;
    }

    public boolean isExist() {
        File file = new File(modifiedUri);
        return file.exists();
    }

    public boolean createFile(Request request) throws IOException {
        return Util.createFile(modifiedUri, request.getBody());
    }

    public boolean deleteFile(Request request) {
        return Util.deleteFile(modifiedUri);
    }

    public boolean isModifiedSince(Request request) throws ParseException {
        String modifiedSinceHeader = request.getHeadersMap().get("If-Modified-Since");
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        Date modifiedSinceDateInHeader = formatter.parse(modifiedSinceHeader);
        File file = new File(modifiedUri);
        Date fileLastModifDate = new Date(file.lastModified());
        return fileLastModifDate.after(modifiedSinceDateInHeader);
    }

    public byte[] fetchResourceFileData() throws IOException {
        File resFile = new File(modifiedUri);
        Path filePath = Paths.get(resFile.getAbsolutePath());
        return Files.readAllBytes(filePath);
    }

    public boolean isScript() {
        return isScript;
    }

    public void setScript(boolean script) {
        isScript = script;
    }
}