package bdd;

import java.util.Vector;
import java.io.*;
import java.util.*;

public class Relation implements Serializable{
    String name;
    Vector<String> column;
    Vector<Vector<Object>> data;
    
///GETTERS     
    public Vector<String> getColumn() {
        return column;
    }
    public Vector<Vector<Object>> getData() {
        return data;
    }
    public String getName() {
        return name;
    }

///SETTERS
    public void setColumn(Vector<String> column) {
        this.column = column;
        // System.out.println(column);
    }
    public void setData(Vector<Vector<Object>> data) {
        this.data = data;
    }
    public void setName(String name) {
        this.name = name;
    }

///CONSTRUCTOR
    public Relation(String n,Vector<String> c,Vector<Vector<Object>> d){
        this.setColumn(c);
        this.setData(d);
        this.setName(n);
    }
    public Relation(File file){
        try{
            this.getFromFile(file);   
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public Relation(){}

///METHODS
    public void createName(File file){
        String name = file.getName().split("\\.")[0];
        setName(name);
    }

    public void createColumn(String l){ //function which create column
        String[] list = l.split("//");
        Vector<String> column = new Vector<String>();
        for(int i = 0 ; i < list.length ; i++){
            column.add(list[i].split(":")[0]);
        } 
        this.setColumn(column);
    }
    
    public void getFromFile(File file) throws Exception{ // function which get the data from the file
        BufferedReader input = new BufferedReader(new FileReader(file.getPath()));
        Vector<Vector<Object>> data_list = new Vector<Vector<Object>>();
        createName(file);
        String temp;
        while((temp = input.readLine()) != null){
            Vector<Object> data = new Vector<Object>();
            String[] list = temp.split("//");
            for(int i = 0 ; i < list.length ; i++){
                data.add(list[i].split(":")[1]);
            }
            data_list.add(data);
            createColumn(temp);
        }
        this.setData(data_list);
    }

    public boolean checkIfgetAll(Vector<String> column){
        if(column.size()==1 && column.get(0).equals("*")){
            return true;
        }else return false;
    }

    public int[] getAllIndexColumn(){
        int[] res = new int[this.getColumn().size()];
        for(int i = 0 ; i < this.getColumn().size() ; i++){
            res[i] = i;
        }
        return res;
    }

    public int[] getColumnIndex(Vector<String> column){
        int[] list_index = new int[column.size()];
        int k = 0;
        for(int j = 0 ; j < column.size() ; j++){
            for(int i = 0 ; i < this.getColumn().size() ; i++){
                if(column.get(j).equals(getColumn().get(i))){
                    // System.out.println(k);
                    list_index[k] = i;
                    k++;
                }
                else if(checkIfgetAll(column)){
                    list_index = this.getAllIndexColumn();
                }
            }
        }
        return list_index;
    }

    public Vector<Vector<Object>> createData(int[] index){
        Vector<Vector<Object>> data_list = new Vector<Vector<Object>>();
        for(int j = 0 ; j < this.getData().size() ; j++){
            Vector<Object> data = new Vector<Object>();
            for(int i = 0 ; i < index.length ; i++){
                // System.out.println(this.getData().get(j).get(index[i]));
                data.add(this.getData().get(j).get(index[i]));
            }
            data_list.add(data);
        }
        return data_list;
    }

    public Vector<String> getListOfColumn(int[] index){
        Vector<String> col = new Vector<String>();
        for(int i = 0 ; i < index.length ; i++){
            col.add(this.getColumn().get(index[i]));
            // System.out.println("colonne ilaina "+col.get(i));
        }
        return col;
    }

    public Relation projection(String tableName , Vector<String> column) throws Exception{
        if(tableName.equals(this.getName())){
            int[] index = getColumnIndex(column);
            // for(int i = 0 ; i < column.size() ; i++){
            for(int i = 0 ; i < index.length ; i++){
                // System.out.println("colonne = "+column.get(i));
                // System.out.println("index = "+index[i]);
            }
            return new Relation(tableName,this.getListOfColumn(index),this.createData(index));
        }
        else{
            throw new Exception("Tsy Misy io Fafana io");
        }
    }

    public Relation select(String tableName , String column , String condition) throws Exception{
        Vector<Vector<Object>> data_list = new Vector<Vector<Object>>();
        if(tableName.equals(this.getName())){
            int index = this.getColumn().indexOf(column);
            if(index == -1){
                throw new Exception("Column doesn't exist");
            }
            for(int i = 0 ; i < this.getData().size() ; i++){
                if( this.getData().get(i).get(index).equals(condition) ){
                    data_list.add(this.getData().get(i));
                }
            }
            return new Relation(tableName,this.getColumn(),data_list);
        }
        else{
            throw new Exception("Tsy Misy io Fafana io");
        }   
    }

    public static Relation union(Relation r1,Relation r2) throws Exception{
        if(r1.getColumn().size() == r2.getColumn().size()){
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            data.addAll(r1.getData());
            data.addAll(r2.getData());
            for(int i = 0 ; i < data.size()-1 ; i++){
                for(int j = i+1 ; j < data.size() ; j++){
                    if(checkIsTheSame(data.get(i),data.get(j))){
                        data.removeElementAt(j);
                    }
                }
            }
            return new Relation("union" , r1.getColumn() , data);
            
        }
        else throw new Exception("Misy Olana");
    }

    public static Relation produitCartesien(Relation r1,Relation r2){
        Vector<String> column = new Vector<String>();
        column.addAll(r1.getColumn());
        column.addAll(r1.getColumn());
        Vector<Vector<Object>> data_list = new Vector<Vector<Object>>();
        for(int i = 0 ; i < r1.getData().size() ; i++){
            for(int j = 0 ; j < r2.getData().size() ; j++){
                Vector<Object> data = new Vector<Object>();
                data.addAll(r1.getData().get(i));
                data.addAll(r2.getData().get(j));
                data_list.add(data);
            }
        }
        return new Relation("produit_Cartesien" , column , data_list);
    }

    public static boolean checkIsTheSame(Vector<Object> obj1,Vector<Object> obj2){
        boolean one = obj1.containsAll(obj2);
        boolean two = obj2.containsAll(obj1);
        if(one && two){
            return true;
        }else return false;
    } 

    public static Relation difference(Relation r1,Relation r2) throws Exception{        
        if(r1.getColumn().size() == r2.getColumn().size()){                
            Vector<Vector<Object>> data_list = new Vector<Vector<Object>>();
            data_list.addAll(r1.getData()); 
            for(int j = 0 ; j < r2.getData().size() ;j++){
                for(int i = 0 ; i < data_list.size() ; i++){
                    if(checkIsTheSame(data_list.get(i),r2.getData().get(j))){
                        data_list.removeElementAt(i);
                    }
                }
            }
            return new Relation("difference" , r1.getColumn() , data_list);
        }
        else throw new Exception();
    }

    public static Relation intersection(Relation r1,Relation r2) throws Exception{
        if(r1.getColumn().size() == r2.getColumn().size()){
            Vector<Vector<Object>> data_list = new Vector<Vector<Object>>();
            for(int i = 0 ; i < r1.getData().size() ; i++){
                for(int j = 0 ; j < r2.getData().size() ;j++){
                    if(checkIsTheSame(r1.getData().get(i),r2.getData().get(j)) == true){
                        Vector<Object> data = new Vector<Object>();
                        data.addAll(r1.getData().get(i));
                        data_list.add(data);
                    }
                }
            }
            return new Relation("intersection" , r1.getColumn() , data_list);
        }
        else throw new Exception();
    }

    public void removeDoublon(){

    }

    public static Relation thetaJointure(Relation r1,Relation r2,String column) throws Exception{
        Relation cartesien = new Relation();
        Relation jointure = new Relation();
        cartesien = produitCartesien(r1, r2);
        int index1 = r1.getIndexColumn(column);
        int index2 = r2.getIndexColumn(column);
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        Vector<String> col = new Vector<String>();
        col.addAll(r1.getColumn());
        col.removeElementAt(index1);
        col.addAll(r2.getColumn());
        jointure.setName("jointure");
        jointure.setColumn(col);
        for(int i = 0 ; i < cartesien.getData().size() ; i++){
            Vector<Object> list = new Vector<Object>();
            if(cartesien.getData().get(i).get(index1).equals(cartesien.getData().get(i).get(index2+r1.getColumn().size()))){
                System.out.println(cartesien.getData().get(i));
                list.addAll(cartesien.getData().get(i));
                list.removeElementAt(index1);
                data.add(list);
            }
        }
        jointure.setData(data);
        return jointure;
    }

    public Relation division(Relation r1,Relation r2)  throws Exception{
        Vector<String> col = difference(r1, r2).getColumn();
        Relation descartes = produitCartesien(r1,r2);
        Relation diff = difference(descartes,r1);
        Relation projection = projection(diff.getName(),col);
        return difference(r1,projection);
    }

    public void displayRelation() {
        int countCol = getColumn().size();
        for (int j = 0; j < countCol; j++) {
            for (int i = 0; i < 15; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
        for (String colonne : getColumn()) {
            System.out.print(colonne);
            for (int i = colonne.length(); i < 15; i++) { System.out.print(" "); }
            System.out.print("|");
        }           
        System.out.println();
        for (int j = 0; j < countCol; j++) {
            for (int i = 0; i < 15; i++) {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
        for (Vector<Object> donnes : getData()) {
            for (Object donne : donnes) {
                if (donne==null) {donne="null";}
                System.out.print(String.valueOf(donne));
                for (int i = String.valueOf(donne).length(); i < 15; i++) {
                    System.out.print(" ");
                }
                System.out.print("|");
            }
            System.out.println();
            for (Object object : donnes) {
                for (int i = 0; i < 15; i++) {
                    System.out.print("-");
                }
                System.out.print("+");
            }
            System.out.println();
        }
    }

    public int getIndexColumn(String column){
        return this.getColumn().indexOf(column);
    }
}