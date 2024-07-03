package com.example.chess.Model;

import com.example.chess.Model.Enums.* ;
import javafx.scene.image.* ;

import java.io.Serializable;

public abstract class Material extends ImageView implements Serializable {
    public Material (Image image, Type type , Color color) {
        super(image);
        this.type = type ;
        this.color = color ;
        this.setFitHeight(50);
        this.setFitWidth(50);
        this.alive = true ;
        this.move = 0 ;
        this.code = counter ;
        counter ++ ;
    }
//
    private static int counter = 10;
    private int code ;

    public int getCode() {
        return code;
    }

    //
    private Type type ;
    private Color color ;
    private Home home = null ;
    private int move ;
    private boolean alive ;
    public int getMove() {
        return move;
    }
    public Type getType() {
        return type;
    }
    public Color getColor() {
        return color;
    }
    public Home getHome() {
        return home;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setMove(int move) {
        this.move = move;
    }
    public void setHome(Home home) {
        this.home = home;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setCode(int code) {
        this.code = code;
    }
}