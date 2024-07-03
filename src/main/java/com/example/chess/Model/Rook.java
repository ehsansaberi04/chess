package com.example.chess.Model;

import com.example.chess.Model.Enums.Color;
import com.example.chess.Model.Enums.Type;
import javafx.scene.image.Image;

public class Rook extends Material{
    public Rook(Image image , Color color) {
        super(image , Type.Rook , color);
    }
}
