package com.example.chess.Model;

import javafx.scene.image.* ;

import java.io.Serializable;

public class Home extends ImageView implements Serializable {
    public Home (Image image , int row , int column) {
        super(image);
        this.row = row ;
        this.column = column ;
    }
    private final int row ;
    private final int column ;
    private Material material = null ;
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
}