/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author mzp7
 */
public class Exam implements Serializable{
    private String coursecode;
    private Date date;
    private LinkedHashMap<Student, Sheet> sheets;
    private String type;
    //private PDDocument examdocument;
    
    public Exam(String coursecode, Date date, String type){
        this.coursecode = coursecode;
        this.date       = date;
        this.type       = type;
        sheets = new LinkedHashMap<>(128);
        //this.examdocument = null;
    }
    public Exam(PDDocument document){
        //this.examdocument = document;
        
        int pagenumber = document.getNumberOfPages();
        BufferedImage[] images = DocOps.convert_to_bufferedimage(document, 600);
        
        Page[] pages = new Page[pagenumber];
        QRcode tempqr = null;
        
        boolean flag = true;
        for(int i = 0; i < pagenumber; i++){
            pages[i] = new Page(images[i]);
            
            if(flag && (tempqr = pages[i].getQRcode()) != null){
                flag = false;
            }
        }
        
        try {
            this.coursecode = tempqr.courseCode;
            this.date       = tempqr.date;
            this.type       = tempqr.examType;        
        } catch (NullPointerException e) {
            System.err.println("Couldn't red any qrcode!");
            
            this.coursecode = "NoN";
            this.date       = new Date();
            this.type       = "NoN"; 
        }
        
        this.sheets = new LinkedHashMap<>(128);
        for(int i = 0; i < pages.length; i++){
            QRcode pageqr = pages[i].getQRcode();
            Student tempstudent = new Student(pageqr.studentName, pageqr.studentNumber);
            
            if (!sheets.containsKey(tempstudent)){
                sheets.put(tempstudent, new Sheet(this));
                sheets.get(tempstudent).setStudent(tempstudent);
            }
            
            sheets.get(tempstudent).addPage(pages[i]);
        }
    }
    
    public Set<Student> getStudents(){
        if(!this.sheets.isEmpty()){
            return this.sheets.keySet();
        }
        return null;
    }
    
    public String getCourseCode(){
        return this.coursecode;
    }
    public Date getDate(){
        return this.date;
    }
    public LinkedHashMap<Student, Sheet> getSheets(){
        return this.sheets;
    }
}
