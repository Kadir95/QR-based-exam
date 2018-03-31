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
public class Page {
    private int pagenumber;
    private ArrayList<Question> questions;
    private Sheet sheet;
    
    public Page(Sheet sheet){
        this.sheet = sheet;
    }
}
