/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Main.SceneSwitch;
import Main.SequenceResult;
import Main.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import Model.CollatzModel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

/**
 * event handler for the project, to be build after scene builder is ready.
 *
 * @author thiago
 */
public class CollatzController{

    //UI Controls
    @FXML private TextField inputField;
    @FXML private VBox chartContainer;
    @FXML private TextArea metricsArea;
    @FXML private Button startBtn;
    @FXML private Button pauseBtn;
    @FXML private Button resetBtn;
    @FXML private Button exportBtn;

    @FXML private MenuItem openCompareMenu;
    @FXML private MenuItem exportMenuItem;
    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem aboutMenu;
    
    private LineChart<Number, Number> chart;
    
    private Timeline timeline;
    private boolean paused = false;
    
    private CollatzModel model = new CollatzModel();
    private Map<Integer, SequenceResult> memo = new ConcurrentHashMap<>();
    
    private static int MAX_RECOMMENDED = 10_000; //can change

    @FXML
    public void initialize() {

        NumberAxis x = new NumberAxis();
        x.setLabel("Step");

        NumberAxis y = new NumberAxis();
        y.setLabel("Value");

        chart = new LineChart<>(x, y);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);

        chartContainer.getChildren().add(chart);
        
        openCompareMenu.setOnAction(e -> {
            try{
                Stage stage = (Stage) chart.getScene().getWindow();
                SceneSwitch.switchTo(stage, "collatz/compareRun.fxml");
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
    }

    /**
     * gets all the information from the CollatzModel class when pressed
     * @param event 
     */
    @FXML
    private void runOnAction(ActionEvent event) {
        List<Integer> seeds = Validation.parseSeeds(inputField.getText());
        if(seeds.isEmpty()){
            //alert method have to add
            alert("Input required", "Input a positive integer.");
            return;
        }
        int num = seeds.get(0);
        
        //validaiton
        String warning = Validation.validateSeed(num, MAX_RECOMMENDED);
        
        chart.getData().clear();
        metricsArea.clear();
        
        //computes full sequence instantly
        SequenceResult result = memo.computeIfAbsent(num, k -> model.calculateSequence(k));
        List<Integer> seq = result.sequence();
        
        double avg = result.avgGrowth();
        String avgFormatted = String.format("%.2f", avg);    
        
        metricsArea.setText(
                "Starting number: " + result.startNum() + "\n" +
                "Steps to 1: " + result.stepsToReachOne() + "\n" +
                "Peak value: " + result.peakNum() + "\n" +
                "Stopping time (ns): " + result.totalStoppingTime() + "\n" +
                "Average growth: " + avgFormatted + "\n\n" +
                "Sequence:\n" + result.sequence());  
    }

    @FXML
    private void pauseOnAction(ActionEvent event) {
    }

    @FXML
    private void resetOnAction(ActionEvent event) {
    }

    @FXML
    private void exportOnAction(ActionEvent event) {
    }
}
