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
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
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
    private NumberAxis compareXAxis, compareYAxis;
    @FXML
    private TextArea compareMetrics;

    private LineChart<Number, Number> compareChart;
    private final CollatzModel model = new CollatzModel();
    private Map<Integer, SequenceResult> memo = new ConcurrentHashMap<>();

    /**
     * Initializes the controller class.
     */
    @FXML
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
        backBtn.setOnAction(e -> {
            try {
                Stage stage = (Stage) compareChart.getScene().getWindow();
                SceneSwitch.switchTo(stage, "collatz/SingleRun.fxml");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
    private void onCompareStart() {
        // 1. Validate and Parse Seeds
        List<Integer> seeds = Validation.parseSeeds(compareInput.getText());
        if (seeds.isEmpty()) {
            compareMetrics.setText("No valid seeds provided. Please enter positive integers.");
            return;
        }

        // 2. Clear previous data
        compareChart.getData().clear();
        compareMetrics.clear();

        StringBuilder metricsOutput = new StringBuilder();

        // 3. Process and Plot Each Seed
        for (int seed : seeds) {

            // Get result (computes or retrieves from cache)
            SequenceResult result = memo.computeIfAbsent(seed, model::calculateSequence);

            // Create Series for the new sequence
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("n=" + seed);

            List<Integer> seq = result.sequence();
            
            // --- PLOTTING LOGIC ADDED HERE ---
            for (int i = 0; i < seq.size(); i++) {
                // Plots the step index (i) vs the value (seq.get(i))
                series.getData().add(new XYChart.Data<>(i, seq.get(i)));
            }
            // --- END PLOTTING LOGIC ---

            compareChart.getData().add(series);

            // Append Metrics
            metricsOutput.append("Seed ").append(seed).append(":\n")
                    .append(" Steps to 1: ").append(result.stepsToReachOne()).append("\n")
                    .append(" Peak: ").append(result.peakNum()).append("\n\n");
        }

        compareMetrics.setText(metricsOutput.toString());
    }

    
    private void showError(String header, String content) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        }
}
