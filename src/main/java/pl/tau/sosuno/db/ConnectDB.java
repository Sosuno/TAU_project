package pl.tau.sosuno.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectDB {

    private static Connection con = null;

    public static Connection getCon()  {

        if(con != null) {
            return con;
        }

        String url = "jdbc:mysql://localhost:8085/tau_project?useLegacyDatetimeCode=false&useSSL=false&serverTimezone=Europe/Amsterdam";
        String username = "root";
        String password = "admin";
        try {
            con = DriverManager.getConnection(url, username,password);
            if(con == null) System.out.println("Connection failed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
