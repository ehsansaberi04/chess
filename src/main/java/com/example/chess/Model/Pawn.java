package com.example.chess.Model;

import com.example.chess.Model.Enums.Color;
import com.example.chess.Model.Enums.Type;
import javafx.scene.image.Image;

public class Pawn extends Material{
    public Pawn(Image image , Color color) {
        super(image , Type.Pawn , color);
        enPassant = false ;
    }
    boolean enPassant ;

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }
}
