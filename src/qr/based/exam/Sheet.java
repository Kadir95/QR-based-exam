/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.util.ArrayList;

/**
 *
 * @author mzp7
 */
public class Sheet {
    private Student student;
    private ArrayList<Page> pages;
    private Exam exam;
    
    public Sheet(Exam exam){
        this.exam = exam;
    }
}
