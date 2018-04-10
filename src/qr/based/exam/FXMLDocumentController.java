/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author mzp7
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    public ImageView   imagelabel;
    @FXML
    public ImageView   errorimageview;
    @FXML
    public Tab         errortab;
    @FXML
    public MenuButton  exammenu;
    @FXML
    public AnchorPane  imagelabelpane;
    @FXML
    public AnchorPane  errorimageviewpane;
    @FXML
    public DataStorage datastorage;
    @FXML
    public MenuButton  methodmenu;
    @FXML
    public TableView<Tablecell> table;
    @FXML
    public TextField   pointenterence;
    @FXML
    public TextArea    reviewfield;
    
    
    private Exam        currentexam = null;
    private Tablecell   currenttablecell = null;
    private String      method  = "QbQ";
    
    class Tablecell{
        public Student student;
        public Question question;
        
        public Tablecell(){
            
        }
        public Tablecell(Student s, Question q){
            this.student = s;
            this.question = q;
        }
    }
    
    @FXML
    private void openmenubuttonaction(ActionEvent event) throws IOException, InterruptedException {
        System.out.println("Open file dialog");
        File file = FileOps.filechooser("Choose a PDF file", "PDF File", new String[]{"*.pdf"});
        
        PDDocument document = PDDocument.load(file);
        datastorage.addExam(document);
        refreshExamMenu();
    }
    
    @FXML
    private void questionbyquestionaction(ActionEvent event){
        this.method = "QbQ";
        methodmenu.setText("Question by Quesiton");
    }
    @FXML
    private void studentbystudentaction(ActionEvent event){
        this.method = "SbS";
        methodmenu.setText("Student by Student");
    }
    
    @FXML
    private void onSaveButton(ActionEvent event){
        this.writeData();
        System.out.println("Data is saved");
    }  
    
    public void writeData(){
        FileRead<DataStorage> fr = new FileRead<>();
        fr.writeListToBinaryFile(new File("data.bin"), datastorage);
        System.out.println("Data stored!");
    }
    
    public void readData(){
        try {
            FileRead<DataStorage> fr = new FileRead<>();

            File file = FileRead.findFile(new File("./"), "data.bin");

            if(file != null){
                datastorage = fr.readBinaryFile(file);
                System.out.println("Data is taken!");
            }
        } catch (IOException e){
            System.err.println("Error at readData | Data cannot read | " + e);
        } catch (ClassNotFoundException e){
            System.err.println("Error at readData | Class cannot be read | " + e);
        }
    }
    
    @FXML
    public void exitApplication(){
        this.writeData();
        System.out.println("bye!");
        Platform.exit();
    }
    
    private void refreshExamMenu(){
        if (!datastorage.exams.isEmpty()){
            Map.Entry<Pair<String, Date>, Exam>[] exams  = new Map.Entry[datastorage.exams.size()];
            exams = (Map.Entry<Pair<String, Date>, Exam>[]) datastorage.exams.entrySet().toArray(exams);
            
            for(int i = 0; i < exams.length; i++){
                MenuItem temp = new MenuItem(exams[i].getKey().getKey() + "+" + exams[i].getKey().getValue().getTime());
                temp.setOnAction((event) -> {
                    String text = ((MenuItem) event.getSource()).getText();
                    String[] arr = text.split("\\+");
                    Pair<String, Date> pair = new Pair<>(arr[0], new Date(Long.valueOf(arr[1])));
                    currentexam = datastorage.exams.get(pair);
                    exammenu.setText(currentexam.getCourseCode());
                    refreshTable();
                });
                exammenu.getItems().add(temp); 
            }
        }
    }
    
    private void refreshTable(){     
        Iterator<Map.Entry<Student, Sheet>> iterator = currentexam.getSheets().entrySet().iterator();
        table.getItems().clear();
        while (iterator.hasNext()) {
            Map.Entry<Student, Sheet> entry = iterator.next();
            ArrayList<Question> quesitons = new ArrayList<>();
            for(int i = 0; i < entry.getValue().getPages().size(); i++){
                for(int j = 0; j < entry.getValue().getPages().get(i).getQuesitons().size(); j++){
                    quesitons.add(entry.getValue().getPages().get(i).getQuestion(j));
                }
            }
            
            for(int i = 0; i < quesitons.size(); i++){
                table.getItems().add(new Tablecell(entry.getKey(), quesitons.get(i)));
                System.out.println("Row : " + entry.getKey().getPair().getKey() + " " + quesitons.get(i).getQuestionNumber());
            }
        }
        table.refresh();
    }
    
    @FXML
    public void nextButtonAction(ActionEvent event){
        
    }
    
    @FXML
    public void backButtonAction(ActionEvent event){
        
    }
    
    public void onTableClick(){
        Tablecell selected = table.getSelectionModel().getSelectedItem();
        currenttablecell = selected;
        initQuestionReview(selected);
    }
    
    public void initQuestionReview(Tablecell selected){
        imagelabel.setImage(SwingFXUtils.toFXImage(selected.question.getImage(), null));
        reviewfield.clear();
        if(selected.question.getReview() != null){
            reviewfield.setText(selected.question.getReview());
        }
        pointenterence.setText(String.valueOf(selected.question.getPoint()));
    }
    
    public void quickSaveReview(){
        if(currenttablecell != null){
            currenttablecell.question.setReview(reviewfield.getText());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imagelabel.fitWidthProperty().bind(imagelabelpane.widthProperty());
        imagelabel.fitHeightProperty().bind(imagelabelpane.heightProperty());
        
        errorimageview.fitWidthProperty().bind(errorimageviewpane.widthProperty());
        errorimageview.fitHeightProperty().bind(errorimageviewpane.heightProperty());
        
        pointenterence.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*\\.\\d*")) {
                    pointenterence.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        reviewfield.setOnKeyReleased((event) -> {
            quickSaveReview();
        });
        
        errortab.setDisable(true);
        datastorage = new DataStorage();
        this.readData();
        refreshExamMenu();
        
        TableColumn<Tablecell, String> questioncolumn = new TableColumn<>("Quesitons");
        TableColumn<Tablecell, String> studentcolumn = new TableColumn<>("Students");

        questioncolumn.setCellValueFactory((param) -> {
            Question quesiton = (Question) param.getValue().question;
            return new ReadOnlyStringWrapper("P :" + String.valueOf(quesiton.getPage().getPageNumber()) + ", Q:" + String.valueOf(quesiton.getQuestionNumber()));
        });

        studentcolumn.setCellValueFactory((param) -> {
            Student student = (Student) param.getValue().student;
            return new ReadOnlyStringWrapper(student.getPair().getKey() + " No: " + String.valueOf(student.getPair().getValue()));
        });

        table.getColumns().add(questioncolumn);
        table.getColumns().add(studentcolumn);
        
        table.setOnMouseClicked((event) -> {
            onTableClick();
        });
    }
}
