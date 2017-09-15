import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileReaderTest {


    public static void main(String[] args) {
        System.out.println("TEST!!!!!!!!!");
        try {
            FileReader fileReader = new FileReader("httpd.conf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}