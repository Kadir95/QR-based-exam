/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private ArrayList<Page> errorpages = new ArrayList<>();
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
            } else {
                errorpages.add(pages[i]);
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
            Student tempstudent = null;
            if(pageqr != null){
                tempstudent = new Student(pageqr.studentName, pageqr.studentNumber);
            } else {
                tempstudent = new Student("NoN", -1);
            }
            
            
            if (!sheets.containsKey(tempstudent)){
                sheets.put(tempstudent, new Sheet(this));
                sheets.get(tempstudent).setStudent(tempstudent);
            }
            
            sheets.get(tempstudent).addPage(pages[i]);
        }
    }
    
    public final ArrayList<Question> quesitonInfo(){
        Sheet sheet = null;
        Iterator iterator = this.sheets.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry enrty = (Map.Entry) iterator.next();
            if(((Sheet) enrty.getValue()).getStudent() != null){
                sheet = ((Sheet) enrty.getValue());
            }
        }
        if(sheet != null){
            ArrayList<Question> quesitons = new ArrayList<>();
            for(Page page : sheet.getPages()){
                for(Question q : page.getQuesitons()){
                    quesitons.add(q);
                }
            }
            return quesitons;
        }
        return null;
    }
    
    public Set<Student> getStudents(){
        if(!this.sheets.isEmpty()){
            return this.sheets.keySet();
        }
        return null;
    }
    
    public String getType(){
        return this.type;
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
    public ArrayList<Page> getErrors(){
        return this.errorpages;
    }
}
