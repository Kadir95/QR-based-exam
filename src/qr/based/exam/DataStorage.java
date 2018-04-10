/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author mzp7
 */
public class DataStorage implements Serializable{
    // Exams HashMap<Pair<String /* Course Code */, Date /* Exam Date */>, Exam>
    public LinkedHashMap<Pair<String, Date>, Exam>   exams;
    
    // Students LinkedHashMap<Long /* Student ID */, Student>
    public LinkedHashMap<Long, Student>             students;
    
    /**
     * 
     * @param exam_maplength        LinkedHashMap's Hash array size for Exam HashMap
     * @param students_maplength    LinkedHashMap's Hash array size for Students LinkedHashMap
     */
    public DataStorage(int exam_maplength, int students_maplength){
        exams = new LinkedHashMap<>(exam_maplength);
        students = new LinkedHashMap<>(students_maplength);
    }
    /**
     * Default lengths:
     *  Exam LinkedHashMap            -> 16
     *  Student LinkedHashMap   -> 128
     */
    public DataStorage(){
        this(16, 128);
    }
    
    /**
     * Create and add a Exam to exams HashMap
     * @param coursecode
     * @param date
     * @param type
     * @return added entry's hashCode 
     */
    private void addExamToMap(Exam exam){
        exams.put(new Pair(exam.getCourseCode(), exam.getDate()), exam);
    }
    
    public void addExam(PDDocument document){
        addExamToMap(new Exam(document));
    }
}
