/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author mzp7
 */
public class Exam {
    private String coursecode;
    private Date date;
    private ArrayList<Sheet> sheets;
    private String type;
    
    public Exam(String coursecode, Date date, String type){
        this.coursecode = coursecode;
        this.date       = date;
        this.type       = type;
        sheets = new ArrayList<>();
    }
}
