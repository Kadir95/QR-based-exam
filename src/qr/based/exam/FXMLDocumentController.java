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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    public Tab         statistictab;
    @FXML
    public Tab         gradetab;
    @FXML
    public MenuButton  exammenu;
    @FXML
    public AnchorPane  imagelabelpane;
    @FXML
    public AnchorPane  errorimageviewpane;
    @FXML
    public DataStorage datastorage;
    @FXML
    public TableView<Tablecell> table;
    @FXML
    public TextField   pointenterence;
    @FXML
    public TextArea    reviewfield;
    @FXML
    public ComboBox<String>    errorstudentcombobox;
    @FXML
    public TreeView<Page>         questionadditiontree;
    @FXML
    public BarChart<String, Float> poinforeachstudent;
    @FXML
    public PieChart     piechart;
    
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
    public void onReviewSaveButton(ActionEvent event){
        quickSaveReview();
        quickSavePoint();
    }
    
    @FXML
    private void onSaveButton(ActionEvent event){
        this.writeData();
        System.out.println("Data is saved");
    }  
    
    @FXML
    public void onExportButtonAciton(ActionEvent event){
        if(currentexam == null){
            return;
        }
        File file = FileOps.savefilechooser();
        if(file != null){
            DocOps.excelExport(currentexam, file);
        }
        
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
                    if(!currentexam.getErrors().isEmpty()){
                        errortab.setDisable(false);
                        //statistictab.setDisable(true);
                        //gradetab.setDisable(true);
                        errorhandle();
                    }
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
                if(entry.getValue().getPages().get(i).getQuesitons().isEmpty()){
                    continue;
                }
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
    public void errorhandle(){
        Iterator<Student> iter = currentexam.getStudents().iterator();
        while(iter.hasNext()){
            errorstudentcombobox.getItems().add(iter.next().getPair().getKey() + "+" + iter.next().getPair().getValue());
        }
        
        
    }
    @FXML
    public void addquestionbutton(ActionEvent event){
        
       
    }
    
    @FXML
    public void nextButtonAction(ActionEvent event){
        onReviewSaveButton(null);
        
        int currentindex = table.getSelectionModel().getSelectedIndex();
        int tablemax = table.getItems().size();
        currentindex++;
        if(currentindex < tablemax){
            currentindex = currentindex % tablemax;
        }
        table.getSelectionModel().select(currentindex);
        
        Tablecell selected = table.getSelectionModel().getSelectedItem();
        currenttablecell = selected;
        initQuestionReview(selected);
    }
    
    @FXML
    public void backButtonAction(ActionEvent event){
        onReviewSaveButton(null);
        
        int currentindex = table.getSelectionModel().getSelectedIndex();
        int tablemax = table.getItems().size();
        currentindex--;
        if(currentindex < 0){
            currentindex = 0;
        }
        table.getSelectionModel().select(currentindex);
        
        Tablecell selected = table.getSelectionModel().getSelectedItem();
        currenttablecell = selected;
        initQuestionReview(selected);
    }
    
    public void onTableClick(){
        onReviewSaveButton(null);
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
        pointenterence.setText("");
        if(selected.question.getPoint() != 0){
            pointenterence.setText(String.valueOf(selected.question.getPoint()));
        } else {
            pointenterence.setPromptText("Between 0 and " + selected.question.getMaxPoint());
        }
        
    }
    
    public void quickSavePoint(){
        if(currenttablecell != null){
            try {
                float point = Float.valueOf(pointenterence.getText());
                if(point >= 0 && point <= currenttablecell.question.getMaxPoint()){
                    currenttablecell.question.setPoint(point);
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The point isn't in interval\nIt must be between 0 and " + currenttablecell.question.getMaxPoint() + ".", ButtonType.YES);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.YES) {
                        alert.close();
                    }
                }
            } catch (NumberFormatException e){
                
            }
        }
    }
    
    public void quickSaveReview(){
        if(currenttablecell != null){
            currenttablecell.question.setReview(reviewfield.getText());
        }
    }
    
    public void statistic(){
        if(currentexam != null){
            float[] pointperquesiton = new float[currentexam.quesitonInfo().size()];
            poinforeachstudent.getData().clear();
            piechart.getData().clear();
            Iterator iter = currentexam.getStudents().iterator();
            while(iter.hasNext()){
                Student std = (Student) iter.next();
                Sheet sheet = currentexam.getSheets().get(std);
                ArrayList<Page> pages = sheet.getPages();
                float totalpoint  = 0;
                int i = 0;
                for(Page page : pages){
                    for(Question quest : page.getQuesitons()){
                        pointperquesiton[i] += quest.getPoint()/quest.getMaxPoint();
                        totalpoint += quest.getPoint();
                        i++;
                    }
                }
                XYChart.Series serie = new XYChart.Series();
                serie.getData().add(new XYChart.Data(std.getPair().getKey(), totalpoint));
                poinforeachstudent.getData().add(serie);
                
                
                
            }
            PieChart.Data[] dataset = new PieChart.Data[pointperquesiton.length];
            for(int j = 0; j < pointperquesiton.length; j++){
                dataset[j] = new PieChart.Data("Queston " + (j + 1), pointperquesiton[j]);
            }
            piechart.getData().addAll(dataset);
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
        pointenterence.setOnKeyPressed((event) -> {
            if(event.getCode() == KeyCode.ENTER){
                quickSavePoint();
            }
        });
        
        reviewfield.setOnKeyReleased((event) -> {
            quickSaveReview();
        });
        
        statistictab.setOnSelectionChanged((event) -> {
            statistic();
        });
        
        errortab.setDisable(false);
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
