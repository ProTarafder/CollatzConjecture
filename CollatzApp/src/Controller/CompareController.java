/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class CompareController implements Initializable {

    @FXML
    private TextField compareInput;
    @FXML
    private Button compareStartBtn, backBtn;
    @FXML
    private VBox compareChartContainer;
    @FXML
    private LineChart<Number, Number> compareChart;
    @FXML
    private NumberAxis compareXAxis, compareYAxis;

    @FXML
    private TextArea compareMetrics;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        NumberAxis x = new NumberAxis();
        x.setLabel("Step");
        
        NumberAxis y = new NumberAxis();
        y.setLabel("Value");
        
        compareChart = new LineChart<>(x, y);
        compareChart.setAnimated(false);
        
        compareChartContainer.getChildren().add(compareChart);
    }

}
