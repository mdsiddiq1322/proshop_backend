package org.mds.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    private static Connection con = null;

    static void createConnection(){
        String url = "jdbc:mysql://localhost:6033/proshop?useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, "root", "Siddiq#1322");
            System.out.println("Connected to database successfully!");
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static
    {
        createConnection();
    }

    public static Connection getConnection(){
        if(con == null){
            createConnection();
        }
        return con;
    }
}
