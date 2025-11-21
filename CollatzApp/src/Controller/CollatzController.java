/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;


import Main.SequenceResult;
import Model.CollatzModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CollatzController implements Initializable {

    // --- FXML ---
    @FXML private TextField inputField;
    @FXML private Button startBtn;
    @FXML private Button pauseBtn;
    @FXML private Button resetBtn;
    @FXML private Button exportBtn;

    @FXML private LineChart<Number, Number> chart;
    @FXML private NumberAxis xAxis;
    @FXML private NumberAxis yAxis;

    @FXML private MenuItem exportMenuItem;
    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem openCompareMenu;
    @FXML private MenuItem aboutMenu;

    @FXML private RadioMenuItem linearScaleMenu;
    @FXML private RadioMenuItem logScaleMenu;

    // --- MODEL ---
    private final CollatzModel model = new CollatzModel();
    private SequenceResult lastResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // chart config
        chart.setAnimated(false);
        chart.setCreateSymbols(false);

        // group scale menu
        ToggleGroup scaleGroup = new ToggleGroup();
        linearScaleMenu.setToggleGroup(scaleGroup);
        logScaleMenu.setToggleGroup(scaleGroup);
        linearScaleMenu.setSelected(true);

        // buttons
        startBtn.setOnAction(e -> runCollatz());
        inputField.setOnAction(e -> runCollatz());
        resetBtn.setOnAction(e -> reset());
        exportBtn.setOnAction(e -> exportCsv());

        // pause does nothing for now
        pauseBtn.setOnAction(e -> {});

        // menu
        exportMenuItem.setOnAction(e -> exportCsv());
        exitMenuItem.setOnAction(e -> closeApp());
        aboutMenu.setOnAction(e -> showAbout());
        openCompareMenu.setOnAction(e -> showCompareTodo());

        // scale switching
        linearScaleMenu.setOnAction(e -> replot());
        logScaleMenu.setOnAction(e -> replot());
    }

    // -------------------------------
    // Run Collatz
    // -------------------------------
    private void runCollatz() {
        String text = inputField.getText();

        int n;
        try {
            n = Integer.parseInt(text.trim());
            if (n <= 0) {
                alert("Input Error", "Enter a positive integer.");
                return;
            }
        } catch (Exception ex) {
            alert("Input Error", "Invalid number.");
            return;
        }

        // compute
        lastResult = model.calculateSequence(n);

        // plot
        plot(lastResult);
    }

    // -------------------------------
    // Plot helper
    // -------------------------------
    private void plot(SequenceResult result) {
        chart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("n = " + result.startNum());

        List<Integer> seq = result.sequence();
        boolean useLog = logScaleMenu.isSelected();

        for (int i = 0; i < seq.size(); i++) {
            double y = seq.get(i);
            if (useLog) {
                y = Math.log10(y);
            }
            series.getData().add(new XYChart.Data<>(i, y));
        }

        yAxis.setLabel(useLog ? "log10(value)" : "Value");
        chart.getData().add(series);
    }

    private void replot() {
        if (lastResult != null) {
            plot(lastResult);
        }
    }

    // -------------------------------
    // Reset
    // -------------------------------
    private void reset() {
        chart.getData().clear();
        inputField.clear();
        lastResult = null;
    }

    // -------------------------------
    // Export CSV
    // -------------------------------
    private void exportCsv() {
        if (lastResult == null) {
            alert("Export Error", "Run a sequence first.");
            return;
        }

        Stage stage = (Stage) chart.getScene().getWindow();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save CSV");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        chooser.setInitialFileName("collatz_" + lastResult.startNum() + ".csv");

        File file = chooser.showSaveDialog(stage);
        if (file == null) return;

        try (PrintWriter out = new PrintWriter(file)) {
            out.println("step,value");
            List<Integer> seq = lastResult.sequence();
            for (int i = 0; i < seq.size(); i++) {
                out.println(i + "," + seq.get(i));
            }
        } catch (IOException ex) {
            alert("Export Error", ex.getMessage());
        }
    }

    // -------------------------------
    // Menu helpers
    // -------------------------------
    private void closeApp() {
        Stage stage = (Stage) chart.getScene().getWindow();
        stage.close();
    }

    private void showAbout() {
        alert("About", "Collatz Visualizer\nSingle-run Mode");
    }

    private void showCompareTodo() {
        alert("Compare Mode", "Compare mode uses a separate controller.\n(Not implemented yet.)");
    }

    // -------------------------------
    // Alerts
    // -------------------------------
    private void alert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(title);
        a.setContentText(msg);
        a.showAndWait();
    }
}