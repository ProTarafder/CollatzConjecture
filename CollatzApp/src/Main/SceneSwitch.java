/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class for switching scenes in the main stage.
 */
public class SceneSwitch {
    private SceneSwitch(){} // Utility class, no instances
    
    /**
     * Switches the stage to a new FXML layout.
     * @param stage The current primary stage.
     * @param fxmlPath The absolute classpath path to the FXML file (e.g., "/View/SingleRun.fxml").
     * @throws IOException if the FXML file is not found.
     */
    public static void switchTo(Stage stage, String fxmlPath) throws IOException{
        
        // Explicitly get the resource URL. This is necessary to debug why the location is null.
        URL location = SceneSwitch.class.getResource(fxmlPath);
        
        if (location == null) {
            // If the URL is null, throw a custom exception with the failing path
            throw new IOException("FXML file not found on classpath: " + fxmlPath);
        }
        
        FXMLLoader loader = new FXMLLoader(location);
        Parent newRoot = loader.load();
        
        //same window, new root
        Scene scene = new Scene(newRoot, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }
}