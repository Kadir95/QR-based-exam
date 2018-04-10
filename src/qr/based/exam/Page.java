/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author mzp7
 */
public class Page implements Serializable{
    private int pagenumber;
    private ArrayList<Question> questions;
    private Sheet sheet;
    private QRcode qrcode;
    private BufferedImage image;
    
    public Page(){
        this.image = null;
        this.pagenumber = 0;
        this.qrcode = null;
        this.questions = null;
        this.sheet = null;
    }
    public Page(BufferedImage image){
        this();
        this.setPage(image);
    }
    public Page(Sheet sheet, BufferedImage image){
        this();
        this.sheet = sheet;
        this.setPage(image);
    }
    
    public int getPageNumber(){
        return this.pagenumber;
    }
    public void setSheet(Sheet sheet){
        this.sheet = sheet;
    }
    public Sheet getSheet(){
        return this.sheet;
    }
    
    public void setQRcode(QRcode qrcode){
        this.qrcode = qrcode;
    }
    public QRcode getQRcode(){
        return this.qrcode;
    }
    public ArrayList<Question> getQuesitons(){
        return this.questions;
    }
    public Question getQuestion(int index){
        if (index < 0 || index >= this.questions.size()){
            return null;
        }
        return this.questions.get(index);
    }
    
    public void setPage(BufferedImage image){
        QRcode tempqr = QRcode.qrcode_parser(image);
        if (tempqr != null){
            this.setPage(tempqr, image);
        } else {
            System.err.println("setPage couldn't read the qr code!");
            this.image = image;
            
        }
    }
    
    public void setPage(QRcode qrcode, BufferedImage image){
        this.qrcode = qrcode;
        this.image = image;
        this.pagenumber = qrcode.pageNumber;
        
        questions = new ArrayList<>(this.qrcode.questions.length);
        
        for(int i = 0; i < this.qrcode.questions.length; i++){
            Question temp = new Question(this);
            
            // Giving the location pair information to question object
            temp.setLocation(this.qrcode.questions[i].location);
            // maxPoint information
            temp.setMaxpoint(this.qrcode.questions[i].maxPoint);
            // Question number info.
            temp.setQuestionNumber(this.qrcode.questions[i].questionNumber);
            // image info. function crops itself image from whole image.
            temp.setImage(image);
            
            this.questions.add(temp);
        }
    }
}
