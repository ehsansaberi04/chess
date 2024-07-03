package com.example.chess.Model;

import com.example.chess.Model.* ;
import com.example.chess.Model.Enums.Type;

import java.io.Serializable;

public class Move implements Serializable {
    public Move (Material material , Target target) {
        this.material = material ;
        this.target = target ;
    }
    public Move (Material material , Target target , Type pawnWinner) {
        this.material = material ;
        this.target = target ;
        this.pawnWinner = pawnWinner ;
    }
    private Material material ;
    private Target target ;
    private Type pawnWinner = null;
    public Material getMaterial() {
        return material;
    }
    public Target getTarget() {
        return target;
    }
    public Type getPawnWinner() {
        return pawnWinner;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }
    public void setTarget(Target target) {
        this.target = target;
    }
    public void setPawnWinner(Type pawnWinner) {
        this.pawnWinner = pawnWinner;
    }
}