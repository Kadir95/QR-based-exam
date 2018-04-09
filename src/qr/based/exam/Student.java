/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import javafx.util.Pair;

/**
 *
 * @author mzp7
 */
public class Student implements Serializable{
    private String  name;
    private long id;
    private ArrayList<Sheet>    sheets;
    
    public Student(){
        this.sheets = new ArrayList<>();
    }
    public Student(String name, long id){
        this();
        this.setInformations(name, id);
    }
    
    public void setInformations(String name, long id){
        this.name   = name;
        this.id     = id;
    }
    
    public Pair<String, Long> getPair(){
        return new Pair<>(this.name, this.id);
    }
    
    public ArrayList<Sheet> getSheets(){
        return this.sheets;
    }
    
    @Override
    public boolean equals(Object student){
        if(student != null){
            if(student instanceof Student){
                if(student == this){
                    return true;
                }
                if(((Student) student).id == this.id && ((Student) student).name.equals(this.name)){
                    return true;
                }
            }
        }
        return false;
    }
    
    // All student objects which have same id and name will have same hashcode.
    @Override
    public int hashCode(){
        return Objects.hash(this.name, this.id);
    }
}
