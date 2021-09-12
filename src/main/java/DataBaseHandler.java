import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseHandler {
    static Connection connection = null;
    public static void init(Connection connection) throws SQLException {
        DataBaseHandler.connection = connection;
        String sqlCreate = "CREATE TABLE IF NOT EXISTS login_record"
                + "  (user           VARCHAR(20),"
                + "   password            VARCHAR(64))";

        Statement stmt = connection.createStatement();
        stmt.execute(sqlCreate);
    }

    public static String register(String login,String password) throws SQLException {
        String passwordhash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);



        String sqlCreate =
                "INSERT INTO login_record (user,password)" +

                 "SLECT * FROM (SELECT '" + login + "','" + passwordhash +"')"+
        " WHERE  NOT EXISTS (SELECT * FROM login_record WHERE user = '" + "' AND password = '" + passwordhash+ "')";

        Statement stmt = connection.createStatement();
        stmt.execute(sqlCreate);
        return "Success";
    }
    public static String login(String login,String password) throws SQLException {
        String passwordhash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        String query = "SELECT COUNT(*) AS total FROM login_record WHERE user='"+ login+"' and password='" + passwordhash + "'";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            if(rs.getInt("total") != 0){
                return "Success";
            }else {
                return "Failed";
            }
        }
        return "Failed";
    }
}
