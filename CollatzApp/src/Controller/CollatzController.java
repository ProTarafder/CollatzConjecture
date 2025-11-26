/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Main.SequenceResult;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
<<<<<<< HEAD
=======
import Model.CollatzModel;
>>>>>>> 76410570124bf2ccef97c6cf9fd7c4e7a959d948

/**
 * event handler for the project, to be build after scene builder is ready.
 *
 * @author thiago
 */
public class CollatzController implements Initializable {

    //UI Controls
    @FXML
    private TextField inputField;
    @FXML
    private Button startBtn, pauseBtn, resetBtn, exportBtn;
    @FXML
<<<<<<< HEAD
    private LineChart<Number, Number> chart;
    @FXML
    private NumberAxis xAxis, yAxis;
    @FXML
    VBox chartContainer;
    @FXML
=======
>>>>>>> 76410570124bf2ccef97c6cf9fd7c4e7a959d948
    private TextArea metricsArea;

    @FXML
    private MenuItem exportMenuItem, exitMenuItem, openCompareMenu, aboutMenu;
    @FXML
    private RadioMenuItem linearScaleMenu;
    @FXML
    private RadioMenuItem logScaleMenu;
    @FXML
    private VBox chartContainer;
    
    private CollatzModel model = new CollatzModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Step");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");
        
        chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        
        chartContainer.getChildren().add(chart);
    }

    /**
     * gets all the information from the CollatzModel class when pressed
     * @param event 
     */
    @FXML
    private void runOnAction(ActionEvent event) {
        int num = Integer.parseInt(inputField.getText());
        SequenceResult result = model.calculateSequence(num);
        double avg = result.avgGrowth();
        String avgFormatted = String.format("%.2f", avg);       
        metricsArea.setText("Starting number: " + result.startNum() + "\n" +
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
