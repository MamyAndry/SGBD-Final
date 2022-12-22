package aff;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import bdd.*;

public class Client extends Thread{
    String query;
    Socket socket;

//SETTERS
    public void setQuery(String query) {
        this.query = query;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

//GETTERS
    public String getQuery() {
        return query;
    }
    public Socket getSocket() {
        return socket;
    }

//CONSTRUCTOR
    public Client(Socket socket){
        this.setSocket(socket);
    }
    public Client(){}

//METHODS
    public static void tableaux(Relation relation){
        if(relation.getName().equals(null)){
            System.out.println("Hamarino Hatakao");
            return;
        }
        else if(relation.getName().equals("createTable")){
            System.out.println("Voaforona ny Fafanao");
            return;
        } 
        else if(relation.getName().equals("createDatabase")){
            System.out.println("Voaforona ny Database");
            return;
        } 
        else if(relation.getName().equals("useDatabase")){
            System.out.println("Voahova ny Database ampiasanao");
            return;
        } 
        else if(relation.getName().equals("insert")){
            System.out.println("Voatsofoka ny sandanao");
            return;
        } 
        relation.displayRelation();
    }

    public void run(){
        // Socket client = new Socket("localhost",1121);
        try{
            DataOutputStream out = new DataOutputStream(this.getSocket().getOutputStream());
            while(true){
                System.out.print("SGBD => ");
                Scanner scan = new Scanner(System.in);
                String query = scan.nextLine();
                out.writeUTF(query);
                out.flush();
                if(query.toLowerCase().equals("exit")){
                    DataInputStream input = new DataInputStream(this.getSocket().getInputStream());
                    String mess = (String) input.readUTF();
                    System.out.println(mess);
                    break;
                }
                else{
                    ObjectInputStream input = new ObjectInputStream(this.getSocket().getInputStream());
                    Relation relation = null;
                    Object obj = input.readObject();
                    if(obj != null && obj instanceof Relation){
                        relation = (Relation)obj;
                        tableaux(relation);
                    }
                    else{
                        Exception e = (Exception)obj;
                        e.printStackTrace();
                    }
                }
                }
            out.close();
            this.getSocket().close();
        }
        catch(Exception e){

        }
    }

    public static void main(String[] args) throws Exception{
        Client client = new Client(new Socket("localhost",1121));
        client.start();
    }
}