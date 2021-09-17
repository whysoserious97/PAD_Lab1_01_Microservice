package loginSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

public class DataBaseHandler {
    static Connection connection = null;
    static HashMap<String,String> loginTokenMap = new HashMap<>();
    static int tokenSize= 18;
    public static void init(Connection connection) throws SQLException {
        DataBaseHandler.connection = connection;
        String sqlCreate = "CREATE TABLE IF NOT EXISTS login_record"
                + "  (user           VARCHAR(20),"
                + "   password            VARCHAR(64))";

        Statement stmt = connection.createStatement();
        stmt.execute(sqlCreate);
    }

    public static Boolean userInDB(String login) throws SQLException{
        String query =
                "SELECT COUNT(*) as total FROM login_record " +

                        "WHERE user = '"+ login +"'";

        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            if(rs.getInt("total") != 0){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
    public static String register(String login,String password) throws SQLException {
        if(!userInDB(login)){
            String passwordhash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);

            String sqlCreate =
                    "INSERT INTO login_record (user,password)" +
                            "SELECT * FROM (SELECT '" + login + "','" + passwordhash +"') as TEMP "+
                            "WHERE NOT EXISTS (SELECT * FROM login_record WHERE user = '" + login + "' AND password = '" + passwordhash+ "')";

            Statement stmt = connection.createStatement();
            stmt.execute(sqlCreate);
            return "Success";
        }
        return "This user already exist";
    }
    public static String login(String login,String password) throws SQLException {
        String passwordhash = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        String query = "SELECT COUNT(*) AS total FROM login_record WHERE user='"+ login+"' and password='" + passwordhash + "'";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            if(rs.getInt("total") != 0){
                if(loginTokenMap.containsKey(login)){
                    return loginTokenMap.get(login);
                }
                String token = "";
                for (int i = 0; i < tokenSize; i++) {
                    int choice = new Random().nextInt(3);
                    int index = 0;
                    switch (choice){
                        case 0: index = new Random().nextInt(10) + 48; // numbers
                        case 1: index = new Random().nextInt(26) + 65; // big letters
                        case 2: index = new Random().nextInt(26) + 97; // small letters
                    }
                    token += (char) index;
                }
                loginTokenMap.put(login,token);
                return token;
            }else {
                return "Failed";
            }
        }
        return "Failed";
    }
}
