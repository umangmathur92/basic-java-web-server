import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private HashMap<String,String> aliases = new HashMap<String, String>();
    private HashMap<String,String> scriptAliases = new HashMap<String, String>();

    public HttpdConf(String filePathStr) throws IOException {
        Path filePath = Paths.get(filePathStr);
        String httpdConfStr = new String(Files.readAllBytes(filePath));
        parseInputStrAndSetAttributes(httpdConfStr);
    }

    public HttpdConf(String serverRoot, String documentRoot, int listenPort, String logFile, String directoryIndex, String accessFileName, HashMap<String, String> aliases, HashMap<String, String> scriptAliases) {
        this.serverRoot = serverRoot;
        this.documentRoot = documentRoot;
        this.listenPort = listenPort;
        this.logFile = logFile;
        this.directoryIndex = directoryIndex;
        this.accessFileName = accessFileName;
        this.aliases = aliases;
        this.scriptAliases = scriptAliases;
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

    public HashMap<String, String> getAliases() {
        return aliases;
    }

    public HashMap<String, String> getScriptAliases() {
        return scriptAliases;
    }

    private void parseInputStrAndSetAttributes(String httpdConfStr) {
        List<String> configStrList = Arrays.asList(httpdConfStr.split("\n"));
        for (String configLineStr: configStrList) {
            String[] str = configLineStr.split(" ");
            List<String> lst = Arrays.asList(configLineStr.split(" "));
            switch (lst.get(0)) {
                case SERVER_ROOT:
                    serverRoot = remQuotesFromStr(lst.get(1));
                    break;
                case DOCUMENT_ROOT:
                    documentRoot = remQuotesFromStr(lst.get(1));
                    break;
                case LISTEN:
                    listenPort = Integer.parseInt(lst.get(1));
                    break;
                case LOG_FILE:
                    logFile = remQuotesFromStr(lst.get(1));
                    break;
                case SCRIPT_ALIAS:
                    scriptAliases.put(lst.get(1), remQuotesFromStr(lst.get(2)));
                    break;
                case ALIAS:
                    aliases.put(str[1], remQuotesFromStr(lst.get(2)));
                    break;
                case DIRECTORY_INDEX:
                    directoryIndex = remQuotesFromStr(lst.get(1));
                    break;
                case ACCESS_FILE_NAME:
                    accessFileName  = remQuotesFromStr(lst.get(1));
                    break;
            }
        }
    }

    private String remQuotesFromStr(String str) {
        return str.replaceAll("\"", "");
    }

    @Override
    public String toString() {
        return "HttpdConf{" +
                "serverRoot='" + serverRoot + '\'' +
                ", documentRoot='" + documentRoot + '\'' +
                ", listenPort=" + listenPort +
                ", logFile='" + logFile + '\'' +
                ", directoryIndex='" + directoryIndex + '\'' +
                ", accessFileName='" + accessFileName + '\'' +
                ", aliases=" + aliases +
                ", scriptAliases=" + scriptAliases +
                '}';
    }
}
