package com.eezywards.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            String user = System.getenv("DBUSERNAME");
            String password = System.getenv("DBPASSWORD");
            String dbname = System.getenv("DBNAME");
            System.out.println("Connecting to MySQL Server: " + serverName);
            System.out.println("Connecting to MySQL Database: " + dbname);
            System.out.println("Connecting with user: " + user);
            
            conex = DriverManager.getConnection("jdbc:mysql://" + serverName + ":3306/" + dbname, user, password);
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conex;
    }
    
    public static void closeConnection(Connection conex) throws SQLException{
        conex.close();
    }
    
    public  void insertProducts(JSONArray products) throws SQLException{
        Connection conex = getConnection();
        Statement st = conex.createStatement();
        for(int i=0;i<products.length();i++){
            PreparedStatement ps = conex.prepareStatement("insert into products (name,price) values (?, ?)");
            JSONObject product = products.getJSONObject(i);
            String name = product.getString("name");
            String price = product.getString("price");
            ps.setString(1, name);
            ps.setString(2, price);
            ps.executeUpdate();
            

        }
        closeConnection(conex);
    }
    
    public  JSONArray getProducts() throws SQLException{
        Connection conex = getConnection();
        Statement st = conex.createStatement();
        ResultSet rs = st.executeQuery("select * from products");
        JSONArray products = new JSONArray();
        while(rs.next()){
            JSONObject product = new JSONObject();
            product.put("name", rs.getString("name"));
            product.put("price", rs.getString("price"));
            products.put(product);
        }
        closeConnection(conex);
        return products;
    }

    public void createAccountBusiness(String businessName, String businessEmail, String ethAddress) throws SQLException {
        Connection conex = getConnection();
        PreparedStatement ps = conex.prepareStatement("insert into accountbusiness (businessName, businessEmail, ethAddress) values (?, ?, ?)");
        ps.setString(1, businessName);
        ps.setString(2, businessEmail);
        ps.setString(3, ethAddress);
        ps.executeUpdate();
        
        closeConnection(conex);
    }

    public JSONObject readAccountBusiness() throws SQLException {
        Connection conex = getConnection();
        Statement st = conex.createStatement();
        ResultSet rs = st.executeQuery("select * from accountbusiness limit 1");
        JSONObject accountBusiness = new JSONObject();
        while(rs.next()){
            accountBusiness.put("businessName", rs.getString("businessName"));
            accountBusiness.put("businessEmail", rs.getString("businessEmail"));
            accountBusiness.put("ethAddress", rs.getString("ethAddress"));
        }
        return accountBusiness;
    }
}