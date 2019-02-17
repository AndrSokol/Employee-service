package employees;

import employees.models.Employee;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBhelper {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/employee_service";

    static final String USER = "root";
    static final String PASS = "root";

    static Connection conn = null;

    public static Connection getConnection(){
        System.out.println("Connecting to DB...");
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Connection getting failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver not found");
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(){
        try {
            System.out.println("Close connecting to DB...");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Closing connection failed");
            e.printStackTrace();
        }
    }
}
