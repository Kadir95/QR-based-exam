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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    
    public static void excelExport(Exam exam, File file){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Table");
        
        ArrayList<Question> quesitons = exam.quesitonInfo();
        
        XSSFRow courserow = sheet.createRow(0);
        courserow.createCell(0).setCellValue("Course : ");
        courserow.createCell(1).setCellValue(exam.getCourseCode());
        
        XSSFRow examtype = sheet.createRow(1);
        examtype.createCell(0).setCellValue("Exam type : ");
        examtype.createCell(1).setCellValue(exam.getType());
        
        XSSFRow questioninfo = sheet.createRow(2);
        questioninfo.createCell(0).setCellValue("Questions ");
        questioninfo.createCell(1).setCellValue("");
        for(int i = 0; i < quesitons.size(); i++){
            questioninfo.createCell(2 + i).setCellValue("Q " + quesitons.get(i).getPage().getPageNumber() + "." + quesitons.get(i).getQuestionNumber());
        }
        
        XSSFRow maxscores = sheet.createRow(3);
        maxscores.createCell(0).setCellValue("Max Points ");
        maxscores.createCell(1).setCellValue("");
        for(int i = 0; i < quesitons.size(); i++){
            maxscores.createCell(2 + i).setCellValue(quesitons.get(i).getMaxPoint());
        }
        
        XSSFRow stdinfo = sheet.createRow(4);
        stdinfo.createCell(0).setCellValue("Student Name");
        stdinfo.createCell(1).setCellValue("Student No");
        
        Iterator<Student> stditerator = exam.getStudents().iterator();
        
        int rownumber = 5;
        while(stditerator.hasNext()){
            XSSFRow student = sheet.createRow(rownumber++);
            Student std = stditerator.next();
            student.createCell(0).setCellValue(std.getPair().getKey());
            student.createCell(1).setCellValue(std.getPair().getValue());
            ArrayList<Question> stdquestions = new ArrayList<>();
            ArrayList<Page> stdpages = exam.getSheets().get(std).getPages();
            for(Page page : stdpages){
                for(Question q : page.getQuesitons()){
                    stdquestions.add(q);
                }
            }
            
            for(int i = 0; i < quesitons.size(); i++){
                student.createCell(2 + i).setCellValue(stdquestions.get(i).getPoint());
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e){
            System.err.println(e);
        } catch (IOException e){
            System.err.println(e);
        }
    }
}
