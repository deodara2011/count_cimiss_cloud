package org.nmic.count;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter {
    public void count(){
        Map<String, DataCount> cimissDcs = getDataCountsFromXugu(Config.getCimissXuguUrl(), Config.getCimissTableNames());
        Map<String, DataCount> cloudDcs = getDataCountsFromXugu(Config.getCloudXuguUrl(), Config.getCloudTableNames());
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(Config.getOutFileName()),"UTF-8"));) {
            writer.write("四级编码,CIMISS表名,云平台表名,CIMISS记录数,云平台记录数数,云平台减CIMISS记录数差值,备注");
            writer.newLine();
            compareAndOutputDataCounts(cimissDcs, cloudDcs, writer);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Done.");
    }
    private Map<String, DataCount> getDataCountsFromXugu(String url, String[] specificTableNames){
        System.out.println("Process url: " + url);
        Map<String, DataCount> dcs = new HashMap<String, DataCount>();
        String beginTime = Config.getBeginTime();
        String endTime = Config.getEndTime();
        XuguConnection conn = new XuguConnection(url);
        for (String tableName : Config.getCommonTableNames()){
            dcs.putAll(conn.getDataCounts(tableName, beginTime, endTime));
            System.out.println("Got data counts from table: " + tableName );
        }
        if (specificTableNames != null){
            for (String tableName : specificTableNames){
                dcs.putAll(conn.getDataCounts(tableName, beginTime, endTime));
                System.out.println("Got data counts from table: " + tableName );
            }
        }
        conn.close();
        return dcs;
    }
    
    private void compareAndOutputDataCounts(Map<String, DataCount> cimissDcs, Map<String, DataCount> cloudDcs,
            BufferedWriter writer) throws IOException{
        Set<Map.Entry<String, DataCount>> entrys = cimissDcs.entrySet();
        for (Map.Entry<String, DataCount> entry : entrys){
            String dataId = entry.getKey();
            DataCount cimissDc = entry.getValue();
            DataCount cloudDc = cloudDcs.get(dataId);
            if (cloudDc != null){
                int diff = cloudDc.getCount() - cimissDc.getCount();
                String remark = "完整性没问题";
                if (diff != 0){
                    remark = "完整性有问题";
                }
                writer.write(String.format("%s,%s,%s,%d,%d,%d,%s\n", dataId, cimissDc.getTableName(), cloudDc.getTableName(), 
                        cimissDc.getCount(), cloudDc.getCount(), diff, remark));
                cloudDcs.remove(dataId);
            }else{
                writer.write(String.format("%s,%s, ,%d, , ,%s\n", dataId, cimissDc.getTableName(),
                        cimissDc.getCount(), "仅CIMISS入库"));
            }
        }
        if (!cloudDcs.isEmpty()){
            for (DataCount dc : cloudDcs.values()){
                writer.write(String.format("%s, ,%s, ,%d, ,%s\n", dc.getDataId(), dc.getTableName(),
                         dc.getCount(), "仅云平台入库"));
            }
        }
    }
}
