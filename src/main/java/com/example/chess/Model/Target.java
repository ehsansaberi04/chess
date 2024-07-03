package com.example.chess.Model;

import com.example.chess.Chess;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.Serializable;

public class Target extends ImageView implements Serializable {
    public Target (Home home) {
        super(image);
        this.home = home ;
        this.setX(home.getX());
        this.setY(home.getY());
        this.setFitWidth(50);
        this.setFitHeight(50);
        this.setOpacity(0.8);
    }
    private Home home ;
    private static final Image image = new Image(Chess.class.getResource("image/target.png").toString());

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }
}
