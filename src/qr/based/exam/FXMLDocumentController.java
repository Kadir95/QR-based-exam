/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.awt.Button;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
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
    
    private Image[] slaytshow = null;
    private int current_slayt_no = 0;
    
    @FXML
    private void openmenubuttonaction(ActionEvent event) throws IOException, InterruptedException {
        System.out.println("Open file dialog");
        File file = FileOps.filechooser("Choose a PDF file", "PDF File", new String[]{"*.pdf"});
        
        PDDocument document = PDDocument.load(file);
        slaytshow = DocOps.convert_to_image(document, 600);
    }
    
    @FXML
    private void save_current_image_button_action(ActionEvent event){
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(slaytshow[current_slayt_no], null), "BMP", new File("output"));
        }catch(IOException e){
            System.err.println("Error at save_current_image_button_action | " + e);
        }
    }
    
    @FXML
    private void nextbuttonaction(ActionEvent event){
        if(slaytshow != null){
            imagelabel.setImage(slaytshow[current_slayt_no]);
            qrlabel.setText(DocOps.readQR(SwingFXUtils.fromFXImage(slaytshow[current_slayt_no], null)));
            current_slayt_no++;
            if(slaytshow.length == current_slayt_no){
                current_slayt_no = 0;
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imagelabel.fitWidthProperty().bind(mainpane.widthProperty());
        imagelabel.fitHeightProperty().bind(mainpane.heightProperty());
    }

}
