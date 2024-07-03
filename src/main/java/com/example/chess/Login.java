package com.example.chess;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private Button buttonSingle;
    @FXML
    private Button buttonMulti;
    @FXML
    private Button buttonOnline;
//    clicked
    @FXML
    void clickedSingle(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Chess.class.getResource("page-game-single.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600 , 400);
            stage.setScene(scene);
            stage.show();
            PageGameSingle.setStage(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void clickedMulti(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Chess.class.getResource("page-game-multi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600 , 400);
            stage.setScene(scene);
            stage.show();
            PageGameMulti.setStage(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clickedOnline(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Chess.class.getResource("page-game-online.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600 , 400);
            stage.setScene(scene);
            stage.show();
            PageGameOnline.setStage(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    entered
    @FXML
    void enteredSingle(MouseEvent event) {
        buttonSingle.setScaleX(1.1);
        buttonSingle.setScaleY(1.1);
    }
    @FXML
    void enteredMulti(MouseEvent event) {
        buttonMulti.setScaleX(1.1);
        buttonMulti.setScaleY(1.1);
    }
    @FXML
    void enteredOnline(MouseEvent event) {
        buttonOnline.setScaleX(1.1);
        buttonOnline.setScaleY(1.1);
    }
//    exited
    @FXML
    void exitedSingle(MouseEvent event) {
        buttonSingle.setScaleX(1);
        buttonSingle.setScaleY(1);
    }
    @FXML
    void exitedMulti(MouseEvent event) {
        buttonMulti.setScaleX(1);
        buttonMulti.setScaleY(1);
    }
    @FXML
    void exitedOnline(MouseEvent event) {
        buttonOnline.setScaleX(1);
        buttonOnline.setScaleY(1);
    }
//    initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
//    stage
    private static Stage stage ;
    public static void setStage (Stage stage) {
        Login.stage = stage ;
    }
}
