package com.eezywards.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

public class DBConnectionM {
    public static Connection getConnection() throws SQLException{
        Connection conex = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String serverName = System.getenv("DBSERVERNAME");
            String user = System.getenv("USERNAME");
            String password = System.getenv("PASSWORD");
            String dbname = System.getenv("DBNAME");
            conex = DriverManager.getConnection("jdbc:mysql://" + serverName + ":3306/" + dbname, user, password);
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conex;
    }
    
    public static void closeConnection(Connection conex) throws SQLException{
        conex.close();
    }
    
    public static void insertProducts(JSONArray products) throws SQLException{
        Connection conex = getConnection();
        Statement st = conex.createStatement();
        for(int i=0;i<products.length();i++){
            JSONObject product = products.getJSONObject(i);
            String name = product.getString("name");
            String price = product.getString("price");
            st.executeUpdate("insert into products (name,  price) values ('"+name+"',  '"+price+"')");
        }
        closeConnection(conex);
    }
    
    public static JSONArray getProducts() throws SQLException{
        Connection conex = getConnection();
        Statement st = conex.createStatement();
        ResultSet rs = st.executeQuery("select * from products");
        JSONArray products = new JSONArray();
        while(rs.next()){
            JSONObject product = new JSONObject();
            product.put("id", rs.getInt("id"));
            product.put("name", rs.getString("name"));
            product.put("price", rs.getString("price"));
            products.put(product);
        }
        closeConnection(conex);
        return products;
    }
}