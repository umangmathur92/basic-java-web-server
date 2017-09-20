package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HtPassword {

    private Map<String,String> logins = new HashMap<String, String>();

    public Map<String, String> getLogins() {
        return logins;
    }
    public void setLogins(Map<String, String> logins) {
        this.logins = logins;
    }

    public void parse(String fileName) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        String str = null;

        while ((str = br.readLine())!=null){

            String[] splitString = str.split(":");

            logins.put(splitString[0], splitString[1].replace("{SHA}", ""));

        }

        br.close();
    }

}