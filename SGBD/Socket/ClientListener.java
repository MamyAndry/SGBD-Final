package listener;

import java.net.*;
import java.io.*;
import java.util.Vector;
import bdd.*;
import data.*;

public class ClientListener extends Thread{
    Socket socket;

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public Socket getSocket() {
        return socket;
    }
    public ClientListener(Socket s){
        setSocket(s);
    }

    public void run(){
        try{
            DataInputStream input  = new DataInputStream(this.getSocket().getInputStream());
            Database db = new Database("root");
            while(true){
                String query = (String) input.readUTF();
                Analyzer a = new Analyzer(query);
                a.setDatabase(db);
                Relation r = a.Analyze();
                if(query.toLowerCase().equals("exit")){  
                    DataOutputStream output  = new DataOutputStream(this.getSocket().getOutputStream());
                    output.writeUTF("EXITED");
                    output.flush();
                    break;
                }
                else{
                    ObjectOutputStream output  = new ObjectOutputStream(this.getSocket().getOutputStream());
                    output.writeObject(r);
                    output.flush();
                }
            }
        }catch(Exception e){
            try{
                ObjectOutputStream output  = new ObjectOutputStream(this.getSocket().getOutputStream());
                output.writeObject(e);
                output.flush();
            }catch(Exception ex){
                
            }
        }
    }
}
