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
import java.util.concurrent.*;

public class JavaServerLikeSystem {

    static Connection connection;
    static String gatewayURL = "http://localhost:8000";
    static ExecutorService service = Executors.newFixedThreadPool(5);
    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/microservice_likes","root","root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public JavaServerLikeSystem() {
    }

    public boolean isStatusUp(){
        System.out.println("Entered");
        return true;
    }

    public String increaseLikes(String id){
        String response = "Failed";
        try {
            DataBaseLikeSystemHandler.increaseLike(id);

        }catch (Exception e){ e.printStackTrace(); }

        return "Liked";
    }

    public String getLikes(String id){

        Callable callable = () -> {

//            Thread.sleep(6000);
            System.out.println("Disturbed Like System - getLikes");
            String response = "None";
            try {
                response =  DataBaseLikeSystemHandler.getLikes(id);
            }catch (Exception e){ e.printStackTrace(); }

            return response;
        };

        Future future = service.submit(callable);

        try{
            return String.valueOf(future.get(5, TimeUnit.SECONDS));
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }catch (TimeoutException e){
            return "None";
        } finally {
            service.shutdown();
        }
        return "None";
    }

    public static void main (String [] args){
        try {

            WebServer server = new WebServer(8102);

            PropertyHandlerMapping mapping = new PropertyHandlerMapping();
            mapping.addHandler("SERVER", JavaServerLikeSystem.class);

            server.getXmlRpcServer().setHandlerMapping(mapping);
            server.start();

            System.out.println("Like System Started successfully.");

            DataBaseLikeSystemHandler.init(connection);

        } catch (Exception exception){
            System.err.println("likeSystem.JavaServer: " + exception);
        }
    }
}