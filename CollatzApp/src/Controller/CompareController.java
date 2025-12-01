/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import Main.SceneSwitch;
import Main.SequenceResult;
import Main.Validation;
import Model.CollatzModel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * event handler for the compare version of the application
 *
 * @author Admin
 */
public class CompareController {

    @FXML
    private TextField compareInput;
    @FXML
    private Button compareStartBtn, backBtn;
    @FXML
    private VBox compareChartContainer;
    @FXML
    private TextArea compareMetrics;

    private LineChart<Number, Number> compareChart;
    private final CollatzModel model = new CollatzModel();
    private Map<Long, SequenceResult> memo = new ConcurrentHashMap<>();

    /**
     * initializes the controller class
     */
    public void initialize() {

        NumberAxis x = new NumberAxis();
        x.setLabel("Step");

        NumberAxis y = new NumberAxis();
        y.setLabel("Value");

        compareChart = new LineChart<>(x, y);
        compareChart.setAnimated(false);
        compareChart.setCreateSymbols(false);
        compareChartContainer.getChildren().add(compareChart);

        compareStartBtn.setOnAction(e -> onCompareStart());
        compareInput.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onCompareStart();
            }
        });
    }
    
    
    private void onCompareStart() {
        // 1. Validate and Parse Seeds with Error Handling
        List<Long> seeds;
        try {
            // This will crash if input contains "h" or other garbage
            seeds = Validation.parseSeeds(compareInput.getText());
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter only valid positive integers separated by commas.\n(Found invalid text)");
            return; // Stops the crash
        }

        if (seeds.isEmpty()) {
            // Uses the existing compareMetrics TextArea to show info, or you could use showError()
            compareMetrics.setText("No valid seeds provided. Please enter positive integers.");
            return;
        }

        // 2. Clear previous data
        compareChart.getData().clear();
        compareMetrics.clear();

        StringBuilder metricsOutput = new StringBuilder();

        // 3. Process and Plot Each Seed
        for (long seed : seeds) {
            // Get result (computes or retrieves from cache)
            SequenceResult result = memo.computeIfAbsent(seed, model::calculateSequence);

            // Create Series for the new sequence
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("n=" + seed);

            List<Long> seq = result.sequence();
            
            // --- PLOTTING LOGIC ---
            for (int i = 0; i < seq.size(); i++) {
                series.getData().add(new XYChart.Data<>(i, seq.get(i)));
            }

            compareChart.getData().add(series);

            // Append Metrics
            metricsOutput.append("Seed ").append(seed).append(":\n")
                    .append(" Steps to 1: ").append(result.stepsToReachOne()).append("\n")
                    .append(" Peak: ").append(result.peakNum()).append("\n\n");
        }

        compareMetrics.setText(metricsOutput.toString());
    }

    /**
     * creates an alert with an error message when the user inputs an invalid input
     * @param header string to be displayed in the header area of the alert
     * @param content the error message to be displayed in the content area
     */
    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * when the user to go back to the single run scene
     * @param event 
     */
    @FXML
    private void backOnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            SceneSwitch.switchTo(stage, "/View/SingleRun.fxml");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}