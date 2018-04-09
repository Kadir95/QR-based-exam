/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.util.Pair;

/**
 *
 * @author mzp7
 */
public class QRcode implements Serializable{
    public String  courseCode;
    public Date    date;
    public String  examType;
    public String  studentName;
    public long    studentNumber;
    public int     pageNumber;
    public questionInfo[] questions;
    public String  qrcodetext;
    
    public static class questionInfo implements Serializable{
        public int      questionNumber;
        public int      maxPoint;
        public Pair<Integer, Integer>  location;
        
        public questionInfo(){
            //Nothing to do
        }
        
        @Override
        public String toString(){
            return ("Number of question     : " + this.questionNumber + "\n" + 
                    "Max point of question  : " + this.maxPoint + "\n" +
                    "Location of question   : " + this.location);
        }
    }
    
    private QRcode(){
        //Nothing to do
    }
    
    public static QRcode[] qrcode_parser(BufferedImage[] image){
        QRcode[] qrcodearray = new QRcode[image.length];
        for(int i = 0; i < image.length; i++){
            qrcodearray[i] = QRcode.qrcode_parser(image[i]);
        }
        return qrcodearray;
    }
    
    public static QRcode qrcode_parser(BufferedImage image){
        String text = DocOps.readQR(image);
        if (text != null){
            return qrcode_parser(text);
        } else {
            System.err.println("There is no QR code in the image!");
            return null;
        }
    }
    
    public static QRcode qrcode_parser(String qrcodecontext){
        QRcode result = new QRcode();
        result.qrcodetext = qrcodecontext;
        
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        
        try{
            String[] parts = qrcodecontext.trim().split(",");
            result.courseCode   = parts[0].trim();
            result.date         = format.parse(parts[1].trim());
            result.examType     = parts[2].trim().toUpperCase().trim();
            result.pageNumber   = Integer.valueOf(parts[3].trim());
            result.studentName  = parts[4].trim();
            result.studentNumber    = Long.valueOf(parts[5].trim());
            int questionQuantity    = parts.length - 6;
            
            result.questions    = new questionInfo[questionQuantity];
            
            for (int i = 0; i < questionQuantity; i++){
                String[] questionInfoParts      = parts[6 + i].trim().split("\\.%");
                String[] questionNumberInfo     = questionInfoParts[0].trim().split("\\.");
                String[] questionLocationInfo   = questionInfoParts[1].trim().split("\\.");
                result.questions[i] = new questionInfo();
                result.questions[i].location    = new Pair(Integer.valueOf(questionLocationInfo[0]), Integer.valueOf(questionLocationInfo[1]));
                result.questions[i].questionNumber  = Integer.valueOf(questionNumberInfo[1].trim());
                result.questions[i].maxPoint    = Integer.valueOf(questionNumberInfo[2].trim());
            }
        } catch (ParseException e){
            System.err.println("Error at qrcode_parser | " + e);
            return null;
        }
        return result;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return ("Course Name    : " + this.courseCode + "\n" + 
                "Student Name   : " + this.studentName + "\n" + 
                "Student Number : " + this.studentNumber + "\n" +
                "Page Number    : " + this.pageNumber + "\n" + 
                "Date           : " + this.date + "\n" + 
                "Exam Type      : " + this.examType + "\n" + 
                "Questions      : \n" + this.questions.toString());
    }
}
