package com.eezywards.user.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

public class DBConnectionUser{
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
    public void createAccountUser(String email, String ethAddress, String nullifier_hash) throws SQLException{
        Connection conex = getConnection();
        PreparedStatement ps = conex.prepareStatement("insert into users (email, eth_address, nullifier_hash) values (?, ?, ?)");

        ps.setString(1, email);
        ps.setString(2, ethAddress);
        ps.setString(3, nullifier_hash);
        ps.executeUpdate();
        closeConnection(conex);
        


    }
    
}