package org.nmic.count;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


public class Config {
    private static String cimissXuguUrl;
    private static String cloudXuguUrl;
    private static String outFileName;
    private static String beginTime;
    private static String endTime;
    private static String[] commonTableNames;
    private static String[] cimissTableNames;
    private static String[] cloudTableNames;
    
    public static void loadConfig(String cfgFileName){
        try(InputStreamReader  isr = new InputStreamReader(new FileInputStream(cfgFileName),"UTF-8")){
            Properties pro = new Properties();
            pro.load(isr);
            cimissXuguUrl = pro.getProperty("cimissXuguUrl");
            cloudXuguUrl = pro.getProperty("cloudXuguUrl");
            outFileName = pro.getProperty("outFileName");
            beginTime = pro.getProperty("beginTime");
            endTime = pro.getProperty("endTime");
            commonTableNames = getStringArrayProperty(pro,"commonTableNames");
            cimissTableNames = getStringArrayProperty(pro,"cimissTableNames");
            cloudTableNames = getStringArrayProperty(pro,"cloudTableNames");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private static String[] getStringArrayProperty(Properties pro, String key){
        String val = pro.getProperty(key);
        if (val == null){
            return null;
        }
        return val.split(",");
    }
    
    public static String getCimissXuguUrl() {
        return cimissXuguUrl;
    }

    public static String getCloudXuguUrl() {
        return cloudXuguUrl;
    }

    public static String getOutFileName() {
        return outFileName;
    }

    public static String getBeginTime() {
        return beginTime;
    }
    public static String getEndTime() {
        return endTime;
    }
    public static String[] getCommonTableNames() {
        return commonTableNames;
    }
    public static String[] getCimissTableNames() {
        return cimissTableNames;
    }
    public static String[] getCloudTableNames() {
        return cloudTableNames;
    }
    
    
}
