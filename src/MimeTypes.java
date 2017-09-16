import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class MimeTypes {

    private HashMap<String, String> mimeTypeMap = new HashMap<>();

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

    }

}
