/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MysqlBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class MysqlDB {
    // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String IP_SERVER = "localhost";
   static final String DB_URL = "jdbc:mysql://"+IP_SERVER+":3306/avengersDB";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";
   
    public static Connection MysqlConnection() {
        Connection conn = null;
        System.err.println(DB_URL);
        try{
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");

           //STEP 3: Open a connection
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.err.println("CONEXION EXITOSA");
           
           return conn;
        }catch(SQLException | ClassNotFoundException se){
           //Handle errors for JDBC or Class.forName
           System.err.println("CONEXION FALLIDA");

        }
        return null;
    }
    
    public static ResultSet MysqlResult(String sql){
       try {
           Connection c;
           c = MysqlDB.MysqlConnection();
           //ResultSet
           ResultSet rs = c.createStatement().executeQuery(sql);
           return rs;
       } catch (SQLException ex) {
           Logger.getLogger(MysqlDB.class.getName()).log(Level.SEVERE, null, ex);
           return null;
       }
       
    }
            
    
    public static void main(String[] args) {
        MysqlDB.MysqlConnection();
    }
}
