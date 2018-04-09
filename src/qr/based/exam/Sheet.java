/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mzp7
 */
public class Sheet implements Serializable{
    private Student student;
    private ArrayList<Page> pages;
    private Exam exam;
    
    public Sheet(Exam exam){
        this.exam = exam;
        this.pages = new ArrayList<>();
    }
    
    public void setStudent(Student studnet){
        this.student = studnet;
    } 
    public Student getStudent(){
        return this.student;
    }
    
    public void addPage(Page page){
        this.pages.add(page);
    }
    public ArrayList<Page> getPages(){
        return this.pages;
    }
}
