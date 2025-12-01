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
 * Event handler for the single run version of the application
 *
 * @author thiago abi
 */
public class CollatzController {

    //UI Controls
    @FXML
    private TextField inputField;
    @FXML
    private VBox chartContainer;
    @FXML
    private TextArea metricsArea;
    @FXML
    private Button startBtn;
    @FXML
    private Button pauseBtn;
    @FXML
    private Button resetBtn;


    @FXML
    private RadioMenuItem linearScaleMenu;
    @FXML
    private RadioMenuItem logScaleMenu;

    private LineChart<Number, Number> chart;
    private XYChart.Series<Number, Number> series;

    private Timeline timeline;
    private int animationIndex = 0;
    private List<Long> currentSequence;
    private final boolean paused = false;

    private long currentStartNum = -1;

    private final CollatzModel model = new CollatzModel();
    private final Map<Long, SequenceResult> memo = new ConcurrentHashMap<>();

    private static int MAX_RECOMMENDED = 1_000_000;

    /**
     * Initializes the controller class
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

        //Allows the code to run by pressing enter
        inputField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                runOnAction(null);
            }
        });

    }

    /**
     * Handles the action when the "Run" button is pressed
     *
     * @param event
     */
    @FXML
    private void runOnAction(ActionEvent event) {
        List<Long> seeds;
        try {
            seeds = Validation.parseSeeds(inputField.getText());
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter only valid positive whole numbers.\n(Found invalid text: '" + inputField.getText() + "')");
            return;
        }

        if (seeds.isEmpty()) {
            showError("Input Required", "Please enter a number.");
            return;
        }

        long num = seeds.get(0);

        // Save the number so Linear/Log scale toggle works
        this.currentStartNum = num;

        String warning = Validation.validateSeed(num, MAX_RECOMMENDED);
        if (warning != null) {
            showWarning("Large Input Warning", warning);
        }

        if (timeline != null) {
            timeline.stop();
        }
        chart.getData().clear();
        metricsArea.clear();
        animationIndex = 0;

        SequenceResult result = memo.computeIfAbsent(num, k -> model.calculateSequence(k));
        currentSequence = result.sequence();

        double avg = result.avgGrowth();
        String avgFormatted = String.format("%.2f", avg);

        metricsArea.setText(
                "Starting number: " + result.startNum() + "\n"
                + "Steps to 1: " + result.stepsToReachOne() + "\n"
                + "Peak value: " + result.peakNum() + "\n"
                + "Stopping time (ns): " + result.totalStoppingTime() + "\n"
                + "Average growth: " + avgFormatted + "\n\n"
                + "Sequence:\n" + result.sequence());

        boolean useLogScale = logScaleMenu.isSelected();
        plotCurrentSequence(num, useLogScale);

        NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        yAxis.setLabel(useLogScale ? "Value (Log Scale)" : "Value");
    }

    /**
     * Plots the Collatz sequence onto the JavaFX chart
     */
    private void plotCurrentSequence(long num, boolean useLogScale) {
        if (timeline != null) {
            timeline.stop();
        }
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
     * Pauses or resumes the plotting animation
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
     * Resets the application state
     */
    @FXML
    private void resetOnAction(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }
        chart.getData().clear();
        metricsArea.clear();
        animationIndex = 0;
        pauseBtn.setText("Pause");
        inputField.clear();
        currentStartNum = -1; // Reset this too
    }

    /**
     * Handles the File -> Export CSV menu item
     */
    @FXML
    private void exportItemOnAction(ActionEvent event) {
        if (metricsArea.getText().isEmpty()) {
            showError("No Data", "Generate a sequence before exporting.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Metrics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("collatz_result.csv");

        // We use chart.getScene() to get the window because the button is gone
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
     * Creates an error alert
     */
    private void showError(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Creates a warning alert
     */
    private void showWarning(String header, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Allows the user to exit the application
     */
    @FXML
    private void exitOnAction(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Allows the user to switch the chart to linear scale
     */
    @FXML
    private void linearScaleOnAction(ActionEvent event) {
        if (currentSequence != null && currentStartNum != -1) {
            ((NumberAxis) chart.getYAxis()).setLabel("Value");
            plotCurrentSequence(currentStartNum, false);
        }
    }

    /**
     * Allows the user to switch the chart to logarithmic scale
     */
    @FXML
    private void logScaleOnAction(ActionEvent event) {
        if (currentSequence != null && currentStartNum != -1) {
            ((NumberAxis) chart.getYAxis()).setLabel("Value (Log Scale)");
            plotCurrentSequence(currentStartNum, true);
        }
    }

    /**
     * Allows the user to switch to compare mode
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

    @FXML
    private void aboutOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Collatz Conjecture Visualization");
        alert.setContentText("A tool to visualize the 3n+1 problem.");
        alert.showAndWait();
    }
}
