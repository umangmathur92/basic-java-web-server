import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MimeTypes {

    private HashMap<String, String> mimeTypeMap = new LinkedHashMap<>();

    public MimeTypes(String filePathStr) throws IOException {
        Path filePath = Paths.get(filePathStr);
        String mimeTypeConfStr = new String(Files.readAllBytes(filePath));
        parseInputStrAndSetAttributes(mimeTypeConfStr);
    }

    public MimeTypes(HashMap<String, String> mimeTypeMap) {
        this.mimeTypeMap = mimeTypeMap;
    }

    public HashMap<String, String> getMimeTypeMap() {
        return mimeTypeMap;
    }

    public void setMimeTypeMap(HashMap<String, String> mimeTypeMap) {
        this.mimeTypeMap = mimeTypeMap;
    }

    private void parseInputStrAndSetAttributes(String mimeTypeConfStr) {
        String[] configLinesArr = mimeTypeConfStr.split("\n");
        for (String configLineStr : configLinesArr) {
            String[] str = configLineStr.split("\\s+");

            if (!configLineStr.startsWith("#") && configLineStr.length() > 0) {
                for (int i = 1; i < str.length; i++) {
                    mimeTypeMap.put(str[i], remQuotesFromStr(str[0]));
                }
            }
        }
    }

    private String remQuotesFromStr(String str) {
        return str.replaceAll("\"", "");
    }

    @Override
    public String toString() {
        return "MimeTypes{" +
                "mimeTypeMap='" + mimeTypeMap + '\'' +
                '}';
    }

}