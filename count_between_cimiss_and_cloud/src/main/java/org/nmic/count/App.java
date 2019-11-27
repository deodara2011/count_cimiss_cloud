package org.nmic.count;

public class App {

    public static void main(String[] args) {
        String cfgFileName = "E:\\NMIC\\work-2019\\大数据云平台\\与CIMISS对比测试\\programs\\count\\cfg.properties";
        if (args.length > 0){
            cfgFileName = args[0];
        }
        Config.loadConfig(cfgFileName);
        new Counter().count();
    }

}
