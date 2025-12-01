/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 * github link:https://github.com/ProTarafder/CollatzConjecture
 */

package Main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main class for the java application
 * @author thiago
 */
public class CollatzApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Initializes two stages: a temporary welcome screen, then the main application
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        Label title = new Label("Collatz Conjecture Visualization");
        title.setStyle("-fx-font-size: 50px; -fx-text-fill: pink; -fx-font-weight: bold;");
        
        Button start = new Button("Start");
        start.setStyle("-fx-font-size: 22px; -fx-padding: 10 35;");
        
        VBox content = new VBox(25, title, start);
        content.setStyle("-fx-alignment: center");
        
        StackPane splashRoot = new StackPane(content);
        splashRoot.setStyle("-fx-background-color: #2b2b2b;");
        
        Scene splashScene = new Scene(splashRoot, 1000, 750, false, SceneAntialiasing.BALANCED);
        
        Stage splashStage = new Stage();
        splashStage.setTitle("Welcome");
        splashStage.setScene(splashScene);
        splashStage.show();
        
        //MAIN APPLICATION
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SingleRun.fxml"));
        
        Scene mainScene = new Scene(loader.load(), 1000, 750);
        primaryStage.setTitle("Collatz Conjecture Visualization");
        primaryStage.setScene(mainScene);
        
        start.setOnAction(e -> {
            splashStage.close(); //close the splash screen
            primaryStage.show(); //opens/shows application
        });
    }
}
