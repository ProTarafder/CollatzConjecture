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
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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
import javafx.scene.input.KeyCode;

/**
 * event handler for the single run version of the application
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
    private List<Long> currentSequence;
    private boolean paused = false;
    private int currentStartNum = -1;
    
    private CollatzModel model = new CollatzModel();
    private Map<Long, SequenceResult> memo = new ConcurrentHashMap<>();
    
    private static int MAX_RECOMMENDED = 10_000; //can change

    /**
     * initializes the controller class
     */
    public void initialize() {

        NumberAxis x = new NumberAxis();
        x.setLabel("Step");

        NumberAxis y = new NumberAxis();
        y.setLabel("Value");

        chart = new LineChart<>(x, y);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chartContainer.getChildren().add(chart);        
        ToggleGroup scaleGroup = new ToggleGroup();
        linearScaleMenu.setToggleGroup(scaleGroup);
        logScaleMenu.setToggleGroup(scaleGroup);

        //allows the code to run by pressing enter
        inputField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                runOnAction(null);
            }
        });
        
    }

    /**
     * handles the action when the "Run" button is pressed, validating the input, calculating its Collatz sequence, and displaying it on the chart
     * @param event 
     */
    @FXML
    private void runOnAction(ActionEvent event) {
        List<Long> seeds;
        try {
            // 1. ATTEMPT TO PARSE
            // If the user typed "2727 h", parseSeeds throws an exception immediately.
            seeds = Validation.parseSeeds(inputField.getText());
            
        } catch (NumberFormatException e) {
            // 2. CATCH INVALID INPUT
            // This block runs if ANY letter or symbol was found.
            showError("Invalid Input", "Please enter only valid positive numbers.\n(Found invalid text: '" + inputField.getText() + "')");
            return; // STOPS THE PROGRAM HERE
        }

        // 3. CHECK FOR EMPTY INPUT
        if (seeds.isEmpty()) {
            showError("Input Required", "Please enter a number.");
            return;
        }
        
        // 4. PROCEED WITH VALID INPUT
        long num = seeds.get(0);
       
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
    
    /**
     * plots the Collatz sequence onto the JavaFX chart in linear or logarithmic scale
     * @param num the starting number of the sequence being plotted
     * @param useLogScale if the chart is plotted in logarithmic or linear scale
     */
    private void plotCurrentSequence(long num, boolean useLogScale) {
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

    /**
     * pauses or resumes the plotting animation for the chart
     * @param event 
     */
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

    /**
     * resets the application state back to its initial condition, clearing results and stopping any running process
     * @param event 
     */
    @FXML
    private void resetOnAction(ActionEvent event) {
        if (timeline != null) timeline.stop();
        chart.getData().clear();
        metricsArea.clear();
        animationIndex = 0;
        pauseBtn.setText("Pause");
        inputField.clear();
    }

    /**
     * allows the user to export the current sequence metrics to a CSV file
     * @param event 
     */
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
    
    /**
     * creates an alert with an error message when the user inputs an invalid input
     * @param header string to be displayed in the header area of the alert
     * @param content the error message to be displayed in the content area
     */
    private void showError(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * creates a warning but still allows the inputted value
     * @param header string to be displayed in the header area of the alert
     * @param content the warning message to be displayed in the content area
     */
    private void showWarning(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
    @FXML
    private void exportItemOnAction(ActionEvent event) {
        // Calls the existing logic
        exportOnAction(event);
    }
    
    /**
     * allows the user to exit the application
     * @param event 
     */
    @FXML
    private void exitOnAction(ActionEvent event) {
        Platform.exit();
    }
    
    /**
     * allows the user to switch the chart to linear scale 
     * @param event 
     */
    @FXML
    private void linearScaleOnAction(ActionEvent event) {
        // Logic to switch to Linear Scale (Animation)
        if (currentSequence != null && currentStartNum != -1) {
            // Set scale, then re-plot
            ((NumberAxis) chart.getYAxis()).setLabel("Value");
            plotCurrentSequence(currentStartNum, false);
        }
    }

    /**
     * allows the user to switch the chart to logarithmic scale
     * @param event 
     */
    @FXML
    private void logScaleOnAction(ActionEvent event) {
        // Logic to switch to Logarithmic Scale (Static Plot)
        if (currentSequence != null && currentStartNum != -1) {
            // Set scale, then re-plot
            ((NumberAxis) chart.getYAxis()).setLabel("Value (Log Scale)");
            plotCurrentSequence(currentStartNum, true);
        }
    }
    
    /**
     * allows the user to switch the scene to be able to compare two different inputs
     * @param event 
     */
    @FXML
    private void openCompareOnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) chart.getScene().getWindow();
            SceneSwitch.switchTo(stage, "/View/CompareRun.fxml"); 
        } catch (Exception ex) {
            showError("Navigation Error", "Could not load Compare View: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * displays information about the Collatz Conjecture
     * @param event 
     */
    @FXML
    private void aboutOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Collatz Conjecture Visualization");
        alert.setContentText("A tool to visualize the 3n+1 problem.");
        alert.showAndWait();
    }
}