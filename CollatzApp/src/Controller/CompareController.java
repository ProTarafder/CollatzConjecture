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
        List<Integer> seeds = Validation.parseSeeds(compareInput.getText());
        if (seeds.isEmpty()) {
            compareMetrics.setText("No valid seeds provided");
            return;
        }

        compareChart.getData().clear();
        compareMetrics.clear();

        String metricsOutput = "";

        for (int seed : seeds) {

            //computes instantly using model
            SequenceResult result = memo.computeIfAbsent(seed, k -> model.calculateSequence(k));

            //series
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("n =" + seed);

            List<Integer> seq = result.sequence();

            //Timeline to animate plotting
            compareChart.getData().add(series);

            metricsOutput
                    = metricsOutput
                    + "Seed " + seed + ":\n"
                    + " Steps to 1: " + result.stepsToReachOne() + "\n"
                    + " Peak: " + result.peakNum() + "\n\n";
        }

        compareMetrics.setText(metricsOutput);
    }
}
