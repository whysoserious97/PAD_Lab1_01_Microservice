package likeSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseLikeSystemHandler {
    static Connection connection = null;
    public static void init(Connection connection) throws SQLException {
        DataBaseLikeSystemHandler.connection = connection;
        String sqlCreate = "CREATE TABLE IF NOT EXISTS likes_table"
                + "  (videoID           VARCHAR(20),"
                + "   likes            INTEGER(255)," +
                            "PRIMARY KEY (videoID))";

        Statement stmt = connection.createStatement();
        stmt.execute(sqlCreate);
    }

    public static void increaseLike(String id) throws SQLException {
            String query = "INSERT INTO likes_table (videoID,likes) VALUES('" + id + "',1) ON DUPLICATE KEY UPDATE likes = likes + 1";
            Statement stmt = connection.createStatement();
            stmt.execute(query);
    }

    public static String getLikes(String id) throws SQLException {
        String query = "SELECT likes FROM likes_table WHERE videoID = '" + id + "'";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()){
            return String.valueOf(rs.getInt("likes"));
        }
        return "None";
    }
}
