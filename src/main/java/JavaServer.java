import org.apache.xmlrpc.*;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class JavaServer {

    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/microservice_login","root","root");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public JavaServer() throws SQLException {
    }

    public Integer sum(int x, int y){
        return new Integer(x+y);
    }


    public Boolean register(String login,String password){
        String response = "";
        try {
            response =  DataBaseHandler.register(login,password);
        }catch (Exception e){ e.printStackTrace(); }

        return response.equals("Success");
    }


    public String login(String login,String password)  {
        String response = "";
        try {
           response =  DataBaseHandler.login(login,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (response.equals("Success")){
            return "Authorized";
        }
        return "Failed to connect";
    }


    public static void main (String [] args){
        try {

            WebServer server = new WebServer(8800);

            PropertyHandlerMapping mapping = new PropertyHandlerMapping();
            mapping.addHandler("SERVER", JavaServer.class);

            server.getXmlRpcServer().setHandlerMapping(mapping);
            server.start();

            System.out.println("Started successfully.");

            DataBaseHandler.init(connection);

        } catch (Exception exception){
            System.err.println("JavaServer: " + exception);
        }
    }
}