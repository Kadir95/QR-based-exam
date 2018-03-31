/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.util.Pair;

/**
 *
 * @author mzp7
 */
public class Question {
    private Image   image;
    private Pair<Integer, Integer> location;
    private float   maxpoint;
    private int question_number;
    private float point;
    private Page page;
    
    public Question(Page page){
        this.page = page;
    }
    
    public void setTmage(Image image){
        this.image = image;
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
        if (location != null){
            return;
        }
        int firstloc = image.getHeight() / 100 * this.location.getKey();
        int lastloc  = firstloc + image.getHeight() / 100 * this.location.getValue();
        
        this.image = SwingFXUtils.toFXImage(image.getSubimage(0, firstloc, image.getWidth(), lastloc), null);
    }
}
