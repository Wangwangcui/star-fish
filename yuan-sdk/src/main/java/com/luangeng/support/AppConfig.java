package com.luangeng.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by LG on 2017/9/23.
 */
public class AppConfig {

    private static final String CONFIG_PATH = System.getProperty("user.dir") + File.separator + "config" + File.separator + "app.properties";

    private static Properties ppt = new Properties();

    static {
        try {
            ppt.load(new FileInputStream(CONFIG_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        if (ppt.contains(key)) {
            return ppt.getProperty(key);
        }
        return null;
    }


}
