package bdd;

import java.io.*;
import java.util.Vector;

public class Database {
    String Path = "DATABASE/";
    String DbPath;
    Vector<Relation> relation;
    
//SETTERS
    public void setDbPath(String DbPath) {
        this.DbPath = DbPath;
    }
    public void setRelation(Vector<Relation> relation) {
        this.relation = relation;
    }
    public void addRelation(Relation relation) {
        this.relation.add(relation);
    }

//GETTERS
    public Vector<Relation> getRelation() {
        return relation;
    }

    public String getPath() {
        return Path;
    }

    public String getDbPath() {
        return DbPath;
    }

//CONSTRUCTOR
    public Database(String database){
        this.useDatabase(database);
        scanFolder();
    }

//METHODS
    public static boolean checkExtension(String path,String extension){
        String[] list = path.split("\\.");
        if(list[list.length-1].equalsIgnoreCase(extension)){
            return true;
        }else return false; 
    }

    public void scanFolder(){
        File file = new File(this.getDbPath());
        File[] list = file.listFiles();
        Vector<Relation> r = new Vector<Relation>();
        for(int i = 0 ; i < list.length ; i++){
            if(checkExtension(list[i].toString(),"bdd")){
                r.add(new Relation(list[i]));
                // System.out.println(list[i].toString());
            }
        }
        setRelation(r);
    }

    public void createDatabase(String database){
        // System.out.println(this.getPath()+database);
        File file = new File(this.getPath()+database);
        file.mkdir();
    }

    public Database useDatabase(String database){
        this.setDbPath(this.getPath()+database);
        return this;
    }

    public void createTable(String name,Vector<String> column){
        File file = new File(this.getDbPath()+"/"+name.toLowerCase()+".bdd");
        Relation temp = new Relation();
        temp.setName(name);
        // System.out.println(column);
        temp.setColumn(column); 
        this.addRelation(temp);
        // System.out.println("ity"+this.getRelation().lastElement().getColumn());
        try{
            file.createNewFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getIndice(String name){
        int index = 0;
        for(int i = 0 ; i < this.getRelation().size() ; i++){
            if(this.getRelation().get(i).getName().equals(name)){
                index = i;
            }
        }
        return index;
    }

    public void insert(String table,Vector<Vector<Object>> data){
        try{
            int index = getIndice(table);
            FileOutputStream insert = new FileOutputStream(new File(this.getDbPath()+"/"+table.toLowerCase()+".bdd"),true);
            String espace = "//";
            String espace_col = ":";
            for(int i = 0; i < data.size() ; i++){
                for(int j = 0 ; j < data.get(i).size() ; j++){
                    if(j == data.get(i).size()-1){
                        espace = "\n";
                    }
                    String d = (String)data.get(i).get(j);
                    // System.out.println("faharoa"+this.getRelation().get(index).getName());
                    // System.out.println("faharoa"+this.getRelation().get(index).getColumn());
                    insert.write(this.getRelation().get(index).getColumn().get(j).getBytes());
                    insert.write(espace_col.getBytes());
                    insert.write(d.getBytes());
                    insert.write(espace.getBytes());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteTemporaryTable(){
        String  union = this.getDbPath()+"union.bdd";
        String  intersection = this.getDbPath()+"intersection.bdd";
        String  difference = this.getDbPath()+"difference.bdd";
        File file1 = new File(union);
        File file2 = new File(intersection);
        File file3 = new File(difference);
        file1.delete();
        file2.delete();
        file3.delete();
    }
}
