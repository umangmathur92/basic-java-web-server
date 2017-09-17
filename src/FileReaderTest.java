public class FileReaderTest {


    public static void main(String[] args) {
        try {
            HttpdConf httpdConf = new HttpdConf("config/httpd.conf");
            Util.print(httpdConf.toString());

            MimeTypes mimeTypeConf = new MimeTypes("config/mime.types");
            Util.print(mimeTypeConf.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}