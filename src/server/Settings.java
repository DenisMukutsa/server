package server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    Properties properties;
    public final static String URL = "url";
    public final static String USER = "user";
    public final static String PASS = "pass";


    public Settings(){
        properties = new Properties();
        File file = new File("test.prop");
        try {
            if(!file.exists())
                file.createNewFile();
            properties.load(new FileReader(file));
        }catch (IOException ex){}
    }


    public String getValue(String key) {
        String value = properties.getProperty(key);
        return value;
    }
}