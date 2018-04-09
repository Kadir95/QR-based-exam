/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.HybridBinarizer;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import java.awt.image.BufferedImage;
/**
 *
 * @author mzp7
 */
public class DocOps {
    public static BufferedImage[] convert_to_bufferedimage(PDDocument document, float dpi){
        BufferedImage[] images = new BufferedImage[document.getNumberOfPages()];
        PDFRenderer docrenderer = new PDFRenderer(document);
        
        for(int i = 0; i < document.getNumberOfPages(); i++){
            try{
                images[i] = docrenderer.renderImageWithDPI(i, dpi);     
            } catch(IOException e){
                System.err.println("Error at convert_to_bufferedimage | " + e);
            }
        }
        
        return images;
    }
    public static Image[] convert_to_image(PDDocument document, float dpi){
        Image[] images = new Image[document.getNumberOfPages()];
        PDFRenderer docrenderer = new PDFRenderer(document);
        
        for(int i = 0; i < document.getNumberOfPages(); i++){
            try{
                images[i] = SwingFXUtils.toFXImage(docrenderer.renderImageWithDPI(i, dpi), null);        
            } catch(IOException e){
                System.err.println("Error at convert_to_image | " + e);
            }
        }
        
        return images;
    }
    
    public static String readQR(BufferedImage image){
        BinaryBitmap binarybitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        try{
            return (new MultiFormatReader().decode(binarybitmap)).getText();
        } catch (NotFoundException e){
            System.err.println("Error at readQR | " + e);
            return null;
        }
    }
}
