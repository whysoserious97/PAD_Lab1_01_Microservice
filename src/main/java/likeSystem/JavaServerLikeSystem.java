package likeSystem;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class JavaServerLikeSystem {

    static Connection connection;
    static String gatewayURL = "http://localhost:8000";
    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/microservice_likes","root","root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public JavaServerLikeSystem() throws SQLException {
    }

    public boolean isStatusUp(){
        return true;
    }

    public String increaseLikes(String id){
        String response = "Failed";
        try {
            DataBaseLikeSystemHandler.increaseLike(id);

        }catch (Exception e){ e.printStackTrace(); }

        return "Liked";
    }

    public int getLikes(String id){
        int response = 0;
        try {
            response =  DataBaseLikeSystemHandler.getLikes(id);
        }catch (Exception e){ e.printStackTrace(); }

        return response;
    }

    public static void main (String [] args){
        try {

            WebServer server = new WebServer(8102);

            PropertyHandlerMapping mapping = new PropertyHandlerMapping();
            mapping.addHandler("SERVERLike", JavaServerLikeSystem.class);

            server.getXmlRpcServer().setHandlerMapping(mapping);
            server.start();

            System.out.println("Like System Started successfully.");

            DataBaseLikeSystemHandler.init(connection);

        } catch (Exception exception){
            System.err.println("likeSystem.JavaServer: " + exception);
        }
    }
}