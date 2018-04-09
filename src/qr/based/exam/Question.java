/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.Serializable;
import javafx.util.Pair;

/**
 *
 * @author mzp7
 */
public class Question implements Serializable{
    private BufferedImage   image;
    private Pair<Integer, Integer> location;
    private float   maxpoint;
    private int question_number;
    private float point;
    private Page page;
    
    public Question(Page page){
        this.page = page;
    }
    
    public void setImage(BufferedImage image){
        this.cutselfy(image);
    }
    public BufferedImage getImage(){
        return this.image;
    }
    public void setLocation(Pair<Integer, Integer> location){
        this.location = location;
    }
    public void setMaxpoint(float maxpoint){
        this.maxpoint = maxpoint;
    }
    public void setQuestionNumber(int questionNumber){
        this.question_number = questionNumber;
    }
    public void givePoint(float point){
        this.point = point;
    }
    
    public void changePage(Page page){
        this.page = page;
    }
    public Page getPage(){
        return this.page;
    }
    
    public void cutselfy(BufferedImage image){
        if (location == null){
            return;
        }
        int firstloc = (int) (image.getHeight() * ((double) this.location.getKey() / 100));
        int lastloc  = (int) (image.getHeight() * ((double) this.location.getValue() / 100));
        try {
            this.image = image.getSubimage(1, firstloc, image.getWidth() - 2, lastloc);    
        } catch (RasterFormatException e){
            System.err.println("Error at cutselfy | " + e);
            this.image = null;
        }
    }
}
