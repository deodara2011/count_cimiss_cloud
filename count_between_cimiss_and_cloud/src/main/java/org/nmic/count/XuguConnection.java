package org.nmic.count;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class XuguConnection {
    private  Connection conn;
    public XuguConnection(String url){
        try {
            Class.forName("com.xugu.cloudjdbc.Driver");
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Map<String, DataCount> getDataCounts(String tableName, String beginTime, String endTime){
        if (conn == null){
            return null;
        }
        String sql = "select '" + tableName + "' as tableName, d_data_id,count(*) from " + tableName
               +" where d_datetime >= ? and d_datetime < ? group by tableName,d_data_id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, beginTime);
            ps.setString(2, endTime);
            ResultSet rs = ps.executeQuery();
            Map<String, DataCount> dcs = new HashMap<String, DataCount>();
            while(rs.next()){
                DataCount dc = new DataCount();
                dc.setTableName(rs.getString(1));
                dc.setDataId(rs.getString(2));
                dc.setCount(rs.getInt(3));
                dcs.put(dc.getDataId(), dc);
            }
            rs.close();
            ps.close();
            return dcs;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public void close(){
        try {
            if (conn != null){
                conn.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
