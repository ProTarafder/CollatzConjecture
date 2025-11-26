/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Main.SceneSwitch;
import Main.SequenceResult;
import Main.Validation;
import Main.CSVExport;
import Model.CollatzModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.RadioMenuItem; 
import javafx.scene.control.ToggleGroup;

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
    
    @FXML private RadioMenuItem linearScaleMenu;
    @FXML private RadioMenuItem logScaleMenu;
    
    private LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> series;
    
    private Timeline timeline;
    private int animationIndex = 0;
    private List<Integer> currentSequence;
    private boolean paused = false;
    private int currentStartNum = -1;
    
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
        // --- SCALE SWITCHING LOGIC ---
        ToggleGroup scaleGroup = new ToggleGroup();
        linearScaleMenu.setToggleGroup(scaleGroup);
        logScaleMenu.setToggleGroup(scaleGroup);
        
        // Listen for menu item change to re-plot the existing data
        scaleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean useLogScale = logScaleMenu.isSelected();
            // Update Y-Axis label
            y.setLabel(useLogScale ? "Value (Log Scale)" : "Value");
            
            // If we have a sequence plotted, re-plot it immediately
            if (currentSequence != null && currentStartNum != -1) {
                // We use the new plot method which doesn't animate in Log scale
                plotCurrentSequence(currentStartNum, useLogScale);
            }
        });

        chartContainer.getChildren().add(chart);
        
        openCompareMenu.setOnAction(e -> {
            try{
                Stage stage = (Stage) chart.getScene().getWindow();
                SceneSwitch.switchTo(stage, "/View/CompareRun.fxml");
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });
        
        exitMenuItem.setOnAction(e -> Platform.exit());
        aboutMenu.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("Collatz Conjecture Visualizer");
            alert.setContentText("A tool to visualize the 3n+1 problem.");
            alert.showAndWait();
        });
        
        exportMenuItem.setOnAction(this::exportOnAction);
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
            showError("Input required", "Input a positive integer.");
            return;
        }
        int num = seeds.get(0);
        
        String warning = Validation.validateSeed(num, MAX_RECOMMENDED);
        if (warning != null) {
           
            showWarning("Large Input Warning", warning);
        }
        
        if (timeline != null) timeline.stop();
        chart.getData().clear();
        metricsArea.clear();
        animationIndex = 0;
        
        chart.getData().clear();
        metricsArea.clear();
        
        //computes full sequence instantly
        SequenceResult result = memo.computeIfAbsent(num, k -> model.calculateSequence(k));
        currentSequence = result.sequence();  
        
        double avg = result.avgGrowth();
        String avgFormatted = String.format("%.2f", avg);    
        
        metricsArea.setText(
                "Starting number: " + result.startNum() + "\n" +
                "Steps to 1: " + result.stepsToReachOne() + "\n" +
                "Peak value: " + result.peakNum() + "\n" +
                "Stopping time (ns): " + result.totalStoppingTime() + "\n" +
                "Average growth: " + avgFormatted + "\n\n" +
                "Sequence:\n" + result.sequence());  
        
    
        boolean useLogScale = logScaleMenu.isSelected();
        plotCurrentSequence(num, useLogScale);
        

        NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        yAxis.setLabel(useLogScale ? "Value (Log Scale)" : "Value");
        
    }
    private void plotCurrentSequence(int num, boolean useLogScale) {
        if (timeline != null) timeline.stop();
        chart.getData().clear();

        series = new XYChart.Series<>();
        series.setName("n=" + num);
        chart.getData().add(series);
        
        if (useLogScale) {
            for (int i = 0; i < currentSequence.size(); i++) {
                double yValue = currentSequence.get(i) > 0 ? Math.log10(currentSequence.get(i)) : 0;
                series.getData().add(new XYChart.Data<>(i, yValue));
            }
        } else {
            animationIndex = 0;
            timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
                if (animationIndex < currentSequence.size()) {
                    series.getData().add(new XYChart.Data<>(animationIndex, currentSequence.get(animationIndex)));
                    animationIndex++;
                } else {
                    timeline.stop(); 
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    @FXML
    private void pauseOnAction(ActionEvent event) {
        if (timeline != null) {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.pause();
                pauseBtn.setText("Resume");
            } else if (timeline.getStatus() == Animation.Status.PAUSED) {
                timeline.play();
                pauseBtn.setText("Pause");
            }
        }
    }

    @FXML
    private void resetOnAction(ActionEvent event) {
        if (timeline != null) timeline.stop();
        chart.getData().clear();
        metricsArea.clear();
        animationIndex = 0;
        pauseBtn.setText("Pause");
        inputField.clear();
    }

    @FXML
    private void exportOnAction(ActionEvent event) {
        if (metricsArea.getText().isEmpty()) {
            showError("No Data", "Generate a sequence before exporting.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Metrics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("collatz_result.csv");
        
        Stage stage = (Stage) chart.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                CSVExport.exportToCSV(metricsArea.getText(), file.getAbsolutePath());
            } catch (IOException ex) {
                showError("Export Error", "Failed to save file: " + ex.getMessage());
            }
        }
    }
    
    private void showError(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showWarning(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
