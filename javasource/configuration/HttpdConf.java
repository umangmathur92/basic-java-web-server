package javasource.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class HttpdConf {

    public static final String SERVER_ROOT = "ServerRoot";
    public static final String DOCUMENT_ROOT = "DocumentRoot";
    public static final String LISTEN = "Listen";
    public static final String LOG_FILE = "LogFile";
    public static final String SCRIPT_ALIAS = "ScriptAlias";
    public static final String ALIAS = "Alias";
    public static final String DIRECTORY_INDEX = "DirectoryIndex";
    public static final String ACCESS_FILE_NAME = "AccessFileName";

    private String serverRoot;
    private String documentRoot;
    private int listenPort;
    private String logFile;
    private String directoryIndex;
    private String accessFileName;
    private HashMap<String,String> aliasesMap = new LinkedHashMap<>();
    private HashMap<String,String> scriptAliasesMap = new LinkedHashMap<>();

    public HttpdConf(String filePathStr) throws IOException {
        Path filePath = Paths.get(filePathStr);
        String httpdConfStr = new String(Files.readAllBytes(filePath));
        parseInputStrAndSetAttributes(httpdConfStr);
    }

    public HttpdConf(String serverRoot, String documentRoot, int listenPort, String logFile, String directoryIndex, String accessFileName, HashMap<String, String> aliasesMap, HashMap<String, String> scriptAliasesMap) {
        this.serverRoot = serverRoot;
        this.documentRoot = documentRoot;
        this.listenPort = listenPort;
        this.logFile = logFile;
        this.directoryIndex = directoryIndex;
        this.accessFileName = accessFileName;
        this.aliasesMap = aliasesMap;
        this.scriptAliasesMap = scriptAliasesMap;
    }

    public String getServerRoot() {
        return serverRoot;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public int getListenPort() {
        return listenPort;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getDirectoryIndex() {
        return directoryIndex;
    }

    public String getAccessFileName() {
        return accessFileName;
    }

    public HashMap<String, String> getAliasesMap() {
        return aliasesMap;
    }

    public HashMap<String, String> getScriptAliasesMap() {
        return scriptAliasesMap;
    }

    private void parseInputStrAndSetAttributes(String httpdConfStr) {
        String[] configLinesArr = httpdConfStr.split("\n");
        for (String configLineStr: configLinesArr) {
            String[] str = configLineStr.split(" ");
            switch (str[0]) {
                case SERVER_ROOT:
                    serverRoot = remQuotesFromStr(str[1]);
                    break;
                case DOCUMENT_ROOT:
                    documentRoot = remQuotesFromStr(str[1]);
                    break;
                case LISTEN:
                    listenPort = Integer.parseInt(str[1]);
                    break;
                case LOG_FILE:
                    logFile = remQuotesFromStr(str[1]);
                    break;
                case SCRIPT_ALIAS:
                    scriptAliasesMap.put(str[1], remQuotesFromStr(str[2]));
                    break;
                case ALIAS:
                    aliasesMap.put(str[1], remQuotesFromStr(str[2]));
                    break;
                case DIRECTORY_INDEX:
                    directoryIndex = remQuotesFromStr(str[1]);
                    break;
                case ACCESS_FILE_NAME:
                    accessFileName  = remQuotesFromStr(str[1]);
                    break;
            }
        }
    }

    private String remQuotesFromStr(String str) {
        return str.replaceAll("\"", "");
    }

    @Override
    public String toString() {
        return "model.HttpdConf{" +
                "serverRoot='" + serverRoot + '\'' +
                ", documentRoot='" + documentRoot + '\'' +
                ", listenPort=" + listenPort +
                ", logFile='" + logFile + '\'' +
                ", directoryIndex='" + directoryIndex + '\'' +
                ", accessFileName='" + accessFileName + '\'' +
                ", aliasesMap=" + aliasesMap +
                ", scriptAliasesMap=" + scriptAliasesMap +
                '}';
    }

}