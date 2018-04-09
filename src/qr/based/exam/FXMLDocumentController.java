/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.Button;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author mzp7
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private ImageView   imagelabel;
    private Button      nextbutton;
    @FXML
    private AnchorPane  mainpane;
    @FXML
    private Label       qrlabel;
    
    DataStorage         datastorage;
    
    @FXML
    private void openmenubuttonaction(ActionEvent event) throws IOException, InterruptedException {
        System.out.println("Open file dialog");
        File file = FileOps.filechooser("Choose a PDF file", "PDF File", new String[]{"*.pdf"});
        
        PDDocument document = PDDocument.load(file);
        datastorage.addExam(document);
    }
    
    @FXML
    private void save_current_image_button_action(ActionEvent event){
        
    }
    
    @FXML
    private void nextbuttonaction(ActionEvent event){
        if (datastorage.exams.isEmpty()){
            return;
        }
        
        Exam[] tempexam = datastorage.exams.values().toArray(new Exam[datastorage.exams.values().size()]);
        Sheet[] tempsheet = tempexam[0].getSheets().values().toArray(new Sheet[tempexam[0].getSheets().values().size()]);
        
        BufferedImage image = tempsheet[0].getPages().get(0).getQuestion(0).getImage();
        
        imagelabel.setImage(SwingFXUtils.toFXImage(image, null));
    }   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imagelabel.fitWidthProperty().bind(mainpane.widthProperty());
        imagelabel.fitHeightProperty().bind(mainpane.heightProperty());
        datastorage = new DataStorage();
    }

}
