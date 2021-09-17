package loginSystem;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class JavaServerLoginSystem {

    static Connection connection;
    static String gatewayURL = "http://localhost:8000";
    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/microservice_login","root","root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public JavaServerLoginSystem() {
    }

    public Boolean isStatusUp(){
        return true;
    }
//    public String requestAutoDiscover(){
//        try {
//            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//            config.setServerURL(new URL(gatewayURL));
//
//            XmlRpcClient server = new XmlRpcClient();
//            server.setConfig(config);
//            Vector params = new Vector();
//
//            params.add("127.0.0.1");
//            params.add(8100);
//
//            Object result = server.execute("autoDiscover",params);
//
//            String response = (String) result;
//            System.out.println("The response is: "+ response);
//            return "requestAutoDiscover:Requested";
//        }catch (Exception e){
//            System.err.println(e.getMessage());
//        }
//
//    return "requestAutoDiscover:Failed";
//    }

    public Boolean register(String login,String password){
        String response = "";
        try {
            response =  DataBaseLoginSystemHandler.register(login,password);
        }catch (Exception e){ e.printStackTrace(); }

        return response.equals("Success");
    }


    public String login(String login,String password)  {
        System.out.println("Service was called");
        String response = "";
        try {
           response =  DataBaseLoginSystemHandler.login(login,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!response.equals("Failed")){
            return response;
        }
        return "Failed";
    }


    public static void main (String [] args){
        try {

            WebServer server = new WebServer(8100);

            PropertyHandlerMapping mapping = new PropertyHandlerMapping();
            mapping.addHandler("SERVER", loginSystem.JavaServerLoginSystem.class);

            server.getXmlRpcServer().setHandlerMapping(mapping);
            server.start();

            System.out.println("Started successfully.");

            DataBaseLoginSystemHandler.init(connection);

        } catch (Exception exception){
            System.err.println("loginSystem.JavaServer: " + exception);
        }
    }
}