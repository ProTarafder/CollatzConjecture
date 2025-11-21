/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collatzapp;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class SceneSwitch {
    private SceneSwitch(){} //Utility class, no instances
    
    //Switches from Stage to new FXML layout
    public static void switchTo(Stage stage, String fxmlPath) throws IOException{
        FXMLLoader loader = new FXMLLoader(SceneSwitch.class.getResource(fxmlPath));
        Parent newRoot = loader.load();
        
        //same window, new root
        Scene scene = new Scene(newRoot, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
    }
}
