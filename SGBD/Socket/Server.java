package aff;

import java.net.*;
import listener.ClientListener;

public class Server {

    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(1121);
        while(true){
            Socket get = server.accept();
            ClientListener cl = new ClientListener(get);
            cl.start();
        }
    }
}
