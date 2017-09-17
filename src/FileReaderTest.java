import model.HttpdConf;
import utilities.Util;

public class FileReaderTest {


    public static void main(String[] args) {
        try {
            HttpdConf httpdConf = new HttpdConf("config/httpd.conf");
            Util.print(httpdConf.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}