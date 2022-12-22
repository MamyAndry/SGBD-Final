package data;

import bdd.*;
import java.util.Vector;

public class Analyzer{
    Database Database;
    String Query;

// SETTERS
    public void setQuery(String Query) {
        this.Query = Query;
    }
    public void setDatabase(Database database) {
        Database = database;
    }

// GETTERS
    public String getQuery() {
        return Query;
    } 
    public Database getDatabase() {
        return Database;
    }   

//CONSTRUCTOR
    public Analyzer(String db){
        setQuery(db);
    }

//METHODS
    public Vector<Vector<Object>> getValueToInsert(String values){
        Vector<Vector<Object>> res = new Vector<Vector<Object>>();
        Vector<Object> val = new Vector<Object>();
        String[] list  = values.split("'");
        String[] list2 = list[1].split(",");
        for(int i = 0 ; i < list2.length ; i++){
            val.add(list2[i]);
        }
        res.add(val);
        return res;
    } 
    public Vector<String> getColumnToInsert(String column){
        Vector<String> res = new Vector<String>();
        String[] list  = column.split("'");
        String[] list2 = list[1].split(",");
        for(int i = 0 ; i < list2.length ; i++){
            res.add(list2[i]);
        }
        return res;
    }

    public Vector<String> getColumns(String column){
        Vector<String> res = new Vector<String>();
        String[] list = column.split(",");
        for(int i = 0 ; i < list.length ; i++){
            res.add(list[i]);
            // System.out.println(res.get(i));
        }
        return res;
    } 
    
    public Relation Analyze() throws Exception{
        System.out.println(this.getQuery());
        String[] list = this.getQuery().split(" ");
        Syntax syntax = new Syntax();
        Relation r = new Relation();
        if(list[0].toLowerCase().equals("tsimpony") && list.length == 4 && list[2].toLowerCase().equals(syntax.getWord()[0])){ //PROJECTION
            this.getDatabase().scanFolder();
            r = getDatabase().getRelation().get(this.getDatabase().getIndice(list[3].toLowerCase()));
            r = r.projection(list[3],getColumns(list[1]));
        }
        else if(list[0].toLowerCase().equals("tsimpony") && list.length == 8 && list[4].toLowerCase().equals(syntax.getWord()[2])){ //SELECT AVEC CONDITION
            this.getDatabase().scanFolder();
            r =  getDatabase().getRelation().get(this.getDatabase().getIndice(list[3].toLowerCase()));
            r = r.select(list[3],list[5],list[7]);
            r = r.projection(r.getName(), this.getColumns(list[1]));
        }
        else if(list[0].toLowerCase().equals("atsofohy") && list[1].toLowerCase().equals(syntax.getWord()[0])){ //INSERTION
            r.setName("insert");
            this.getDatabase().insert(list[2],this.getValueToInsert(list[3]));
        }
        else if(list[0].toLowerCase().equals("ampiraiso") && list[2].toLowerCase().equals(syntax.getWord()[3])){ //UNION
            this.getDatabase().scanFolder();
            Relation r1 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[1].toLowerCase()));
            Relation r2 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[3].toLowerCase()));
            r = Relation.union(r1, r2);
        }
        else if(list[0].toLowerCase().equals("ampifanapaho") && list[2].toLowerCase().equals(syntax.getWord()[3])){ // INTERSECTION
            this.getDatabase().scanFolder();
            Relation r1 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[1]));
            Relation r2 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[3]));
            r = Relation.intersection(r1, r2);
        }
        else if(list[0].toLowerCase().equals("ampitombohy") && list[2].toLowerCase().equals(syntax.getWord()[3])){ // PRODUIT CARTESIEN
            this.getDatabase().scanFolder();
            Relation r1 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[1].toLowerCase()));
            Relation r2 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[3].toLowerCase()));
            r = Relation.produitCartesien(r1, r2);
        }
        else if(list[0].toLowerCase().equals("tsimpony") && list[2].toLowerCase().equals(syntax.getWord()[0]) && list[4].toLowerCase().equals("ampiharaho") && list[6].toLowerCase().equals(syntax.getWord()[4])){ //JOINTURE
            this.getDatabase().scanFolder();
        Relation r1 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[3].toLowerCase()));
            Relation r2 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[5].toLowerCase()));
            r = Relation.thetaJointure(r1,r2,list[7]);
            r.projection(r.getName(), this.getColumns(list[1]));
        }
        else if(list[0].toLowerCase().equals("fahasamihafana")  && list[2].toLowerCase().equals(syntax.getWord()[3])){// DIFFERENCENCE
            this.getDatabase().scanFolder();
            Relation r1 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[1].toLowerCase()));
            Relation r2 = getDatabase().getRelation().get(this.getDatabase().getIndice(list[3].toLowerCase()));
            r = Relation.difference(r1, r2);
        }
        else if(list[0].toLowerCase().equals("tsimpony") && list[2].toLowerCase().equals(syntax.getWord()[0]) && list[3].toLowerCase().equals("/")){ //SOUS-REQUETE
            if(list[list.length-1].equals("/")){
                String request = list[4];
                for(int i = 5 ; i < list.length-1 ; i++){
                    request+=" "+list[i];
                }
                // System.out.println(request);
                Analyzer a = new Analyzer(request);
                a.setDatabase(this.getDatabase());
                r = a.Analyze();
                // System.out.println(r.getColumn());
                r = r.projection(r.getName(),getColumns(list[1]));
                }
            else throw new Exception("Iangaviana mba hanidy ny 'sous-requete' ianao");
        }
        else if(list[0].toLowerCase().equals("ampiasao")){ // USE DATABASE 
            Database database  = this.getDatabase().useDatabase(list[1]);
            r.setName("useDatabase");
            this.setDatabase(database);
        }
        else if(list[0].toLowerCase().equals("mamorona")){ // CREATE DATABASE 
            r.setName("createDatabase");
            this.getDatabase().createDatabase(list[1]);
        }
        else if(list[0].toLowerCase().equals("manamboara")){ // CREATE TABLE
            r.setName("createTable");
            this.getDatabase().createTable(list[1],this.getColumnToInsert(list[2]));
        }
        else throw new Exception("Hamarino ny hatakao");
        return r;
    }
}