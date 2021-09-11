import org.apache.xmlrpc.*;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.webserver.WebServer;

public class JavaServer {

    public Integer sum(int x, int y){
        return new Integer(x+y);
    }

    public static void main (String [] args){

        try {

            System.out.println("Attempting to start XML-RPC Server...");

            WebServer server = new WebServer(8800);

            PropertyHandlerMapping mapping = new PropertyHandlerMapping();
            mapping.addHandler("SERVER", JavaServer.class);

            server.getXmlRpcServer().setHandlerMapping(mapping);
            server.start();

            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");

        } catch (Exception exception){
            System.err.println("JavaServer: " + exception);
        }
    }
}