
package mysqlConnect;

import java.sql.*;

import javax.swing.JOptionPane;
public class MysqlConnect {
    public static Connection connectDB() {
        String host = "jdbc:mysql://localhost:3306/2com2?useSSL=true&useUnicode=yes&characterEncoding=UTF-8";
        String username = "root";
        String password = "";
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(host, username, password);
            //JOptionPane.showMessageDialog(null, "Connected");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return conn;
    }
    public static void main(String[] args) {
        connectDB();
    }
}
