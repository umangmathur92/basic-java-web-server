import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class MimeTypes {

    private HashMap<String, List<String>> mimeTypeMap = new HashMap<>();

    public MimeTypes(String filePathStr) throws IOException {
        Path filePath = Paths.get(filePathStr);
        String mimeTypeConfStr = new String(Files.readAllBytes(filePath));
        parseInputStrAndSetAttributes(mimeTypeConfStr);
    }

    public MimeTypes(HashMap<String, List<String>> mimeTypeMap) {
        this.mimeTypeMap = mimeTypeMap;
    }

    public HashMap<String, List<String>> getMimeTypeMap() {
        return mimeTypeMap;
    }

    public void setMimeTypeMap(HashMap<String, List<String>> mimeTypeMap) {
        this.mimeTypeMap = mimeTypeMap;
    }

    private void parseInputStrAndSetAttributes(String mimeTypeConfStr) {

    }

}
