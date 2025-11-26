/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author thiago
 */
public class CollatzApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Add an image for background
        ImageView background = new ImageView(new Image(
                getClass().getResource("").toExternalForm()));
        background.setFitWidth(1000);
        background.setFitHeight(750);
        background.setPreserveRatio(false);
        
        Label title = new Label("Collatz Conjecture Visualization");
        title.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button start = new Button("Start");
        start.setStyle("-fx-font-size: 22px; -fx-padding: 10 35;");
        
        VBox content = new VBox(25, title, start);
        content.setStyle("-fx-alignment: center");
        
        StackPane splashRoot = new StackPane(background, content);
        Scene splashScene = new Scene(splashRoot, 1000, 750, false, SceneAntialiasing.BALANCED);
        
        Stage splashStage = new Stage();
        splashStage.setTitle("Welcome");
        splashStage.setScene(splashScene);
        splashStage.show();
        
        //MAIN APPLICATION
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SingleRun.fxml"));
        
        Scene mainScene = new Scene(loader.load(), 1000, 750);
        primaryStage.setTitle("Collatz Conjecture Visualization");
        primaryStage.setScene(mainScene);
        
        start.setOnAction(e -> {
            splashStage.close(); //close the splash screen
            primaryStage.show(); //opens/shows application
        });
    }
}
