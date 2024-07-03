package com.example.chess;

import com.example.chess.Exceptions.* ;
import com.example.chess.Model.* ;
import com.example.chess.Model.Enums.* ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.* ;
import javafx.scene.layout.* ;
import javafx.scene.text.* ;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.* ;
import static java.lang.Math.abs;
import static java.lang.Math.random;

public class PageGameSingle implements Initializable {
    @FXML
    private AnchorPane pane;
    private ImageView cancelWhite = new ImageView(new Image(Chess.class.getResource("image/cancel.png").toString()));
//    private ImageView cancelBlack = new ImageView(new Image(Chess.class.getResource("image/cancel.png").toString()));
    //    initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        creatHomes();
        creatWhiteMaterials();
        creatBlackMaterials();
        creatOtherButtons();
        tern = Tern.White;
        onGame = true ;
    }
    private static Stage stage ;
    public static void setStage(Stage stage) {
        PageGameSingle.stage = stage;
    }
    private final List<Home> homes = new ArrayList<>();
    private final List<Material> whiteMaterials = new ArrayList<>();
    private final List<Material> blackMaterials = new ArrayList<>();
    private final List<Home> targets = new ArrayList<>();
    private final List<Target> targetList = new ArrayList<>();
    private void creatHomes() {
        Image image1 = new Image(Chess.class.getResource("image/home-black.png").toString());
        Image image2 = new Image(Chess.class.getResource("image/home-white.png").toString());
        int row = 1;
        int column = 1;
        for (int i = 0; i < 64; i++) {
            if (row > 8) {
                row -= 8;
                column++;
            }
            Home home;
            if ((row + column) % 2 == 0) {
                home = new Home(image1, row, column);
            } else {
                home = new Home(image2, row, column);
            }
            home.setFitHeight(50);
            home.setFitWidth(50);
            home.setX(100 + (row - 1) * 50);
            home.setY(350 - (column - 1) * 50);
            pane.getChildren().add(home);
            homes.add(home);
            row++;
        }
    }
    private void creatOtherButtons () {
        pane.getChildren().add(cancelWhite);
        cancelWhite.setFitHeight(50);
        cancelWhite.setFitWidth(50);
        cancelWhite.setX(25);
        cancelWhite.setY(325);
        cancelWhite.setVisible(false);
        cancelWhite.setOnMouseClicked(event -> {
            for (Target target : targetList) {
                target.setVisible(false);
            }
            targetList.clear();
            targets.clear();
            chosen = null;
            cancelWhite.setVisible(false);
        });

//        pane.getChildren().add(cancelBlack);
//        cancelBlack.setFitHeight(50);
//        cancelBlack.setFitWidth(50);
//        cancelBlack.setX(525);
//        cancelBlack.setY(25);
//        cancelBlack.setVisible(false);
//        cancelBlack.setOnMouseClicked(event -> { });
    }
    private Home findHome(int row, int column) throws NoHomeException {
        for (Home home : homes) {
            if (home.getRow() == row && home.getColumn() == column) {
                return home;
            }
        }
        throw new NoHomeException();
    }
    private void picking(Material material, Home home) {
        if (material.getHome() != null) {
            material.getHome().setMaterial(null);
        }
        if (home.getMaterial() != null) {
            destroyMaterial(home.getMaterial());
        }
        material.setHome(home);
        material.setX(home.getX());
        material.setY(home.getY());
        home.setMaterial(material);
    }
    private void destroyMaterial(Material material) {
        if (material != null) {
            if (material.getColor().equals(Color.White)) {
                material.setVisible(false);
                whiteMaterials.remove(material);
                material.setAlive(false);
            } else if (material.getColor().equals(Color.Black)) {
                material.setVisible(false);
                blackMaterials.remove(material);
                material.setAlive(false);
            }
        }
    }
    private boolean checkTarget(Home home) {
        return home.getMaterial() != null;
    }
    private Material chosen = null;
    private Tern tern;
    private boolean onGame ;
    private void changeTern(Tern temp) {
        tern = temp;
    }
    private void showNoTern() {

    }
    private void showEndGame (Result result) {
        cancelWhite.setVisible(false);
        Pane endPane = new Pane() ;
        pane.getChildren().add(endPane) ;
        endPane.setPrefHeight(400);
        endPane.setPrefWidth(250);
        endPane.setLayoutX(100);
        endPane.setLayoutY(75);
        ImageView back = new ImageView(new Image(Chess.class.getResource("image/back-end.png").toString())) ;
        endPane.getChildren().add(back) ;
        back.setOpacity(0.6);
        Text text = new Text();
        endPane.getChildren().add(text) ;
        switch (result) {
            case WhiteWin -> text.setText("white win");
            case BlackWin -> text.setText("black win");
            case Equal -> text.setText("game be equal");
        }
        text.setFont(new Font(20));
        text.setStrokeWidth(100);
        text.setX(150);
        text.setY(100);
        Button button = new Button("back");
        endPane.getChildren().add(button) ;
        button.setPrefWidth(100);
        button.setPrefHeight(30);
        button.setLayoutX(150);
        button.setLayoutY(200);
        button.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Chess.class.getResource("login.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600 , 400);
                stage.setScene(scene);
                stage.show();
                Login.setStage(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    //    white
    private King whiteKing;
    private Queen whiteQueen;
    private Bishop whiteBishop1;
    private Bishop whiteBishop2;
    private Knight whiteKnight1;
    private Knight whiteKnight2;
    private Rook whiteRook1;
    private Rook whiteRook2;
    private boolean whiteEnPassant;
    private void creatWhiteMaterials() {
//        images
        Image whiteKingImage = new Image(Chess.class.getResource("image/king-white.png").toString());
        Image whiteQueenImage = new Image(Chess.class.getResource("image/queen-white.png").toString());
        Image whiteBishopImage = new Image(Chess.class.getResource("image/bishop-white.png").toString());
        Image whiteKnightImage = new Image(Chess.class.getResource("image/knight-white.png").toString());
        Image whiteRookImage = new Image(Chess.class.getResource("image/rook-white.png").toString());
//        classes
        whiteKing = new King(whiteKingImage, Color.White);
        whiteQueen = new Queen(whiteQueenImage, Color.White);
        whiteBishop1 = new Bishop(whiteBishopImage, Color.White);
        whiteBishop2 = new Bishop(whiteBishopImage, Color.White);
        whiteKnight1 = new Knight(whiteKnightImage, Color.White);
        whiteKnight2 = new Knight(whiteKnightImage, Color.White);
        whiteRook1 = new Rook(whiteRookImage, Color.White);
        whiteRook2 = new Rook(whiteRookImage, Color.White);
//        add to pane
        pane.getChildren().add(whiteKing);
        pane.getChildren().add(whiteQueen);
        pane.getChildren().add(whiteBishop1);
        pane.getChildren().add(whiteBishop2);
        pane.getChildren().add(whiteKnight1);
        pane.getChildren().add(whiteKnight2);
        pane.getChildren().add(whiteRook1);
        pane.getChildren().add(whiteRook2);
//        add to list
        whiteMaterials.add(whiteKing);
        whiteMaterials.add(whiteQueen);
        whiteMaterials.add(whiteBishop1);
        whiteMaterials.add(whiteBishop2);
        whiteMaterials.add(whiteKnight1);
        whiteMaterials.add(whiteKnight2);
        whiteMaterials.add(whiteRook1);
        whiteMaterials.add(whiteRook2);
//        picking
        try {
            picking(whiteRook1, findHome(1, 1));
            picking(whiteKnight1, findHome(2, 1));
            picking(whiteBishop1, findHome(3, 1));
            picking(whiteQueen, findHome(4, 1));
            picking(whiteKing, findHome(5, 1));
            picking(whiteBishop2, findHome(6, 1));
            picking(whiteKnight2, findHome(7, 1));
            picking(whiteRook2, findHome(8, 1));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
//        pawns
        creatWhitePawns();
//        functions
        whiteKing.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteKing;
                int x = whiteKing.getHome().getRow();
                int y = whiteKing.getHome().getColumn();
                for (Home home : homes) {
                    if (abs(home.getRow() - x) <= 1) {
                        if (abs(home.getColumn() - y) <= 1) {
                            if (safeMoveWhite(whiteKing, home)) {
                                addWhiteTarget(home);
                            }
                        }
                    }
                }
                if (whiteKing.getMove() == 0) {
                    boolean kingRook1 = true;
                    boolean kingRook2 = true;
                    for (int i = x - 1; i >= 2; i--) {
                        try {
                            Home home = findHome(i, y);
                            if (checkTarget(home)) {
                                kingRook1 = false;
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    for (int i = x + 1; i <= 7; i++) {
                        try {
                            Home home = findHome(i, y);
                            if (checkTarget(home)) {
                                kingRook2 = false;
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    try {
                        if (whiteKing.getMove() == 0) {
                            if (kingRook1 && whiteRook1.isAlive()) {
                                if (whiteRook1.getMove() == 0) {
                                    if (safeMoveWhite(whiteKing, findHome(4, 1))) {
                                        if (safeMoveWhite(whiteKing, findHome(3, 1))) {
                                            addWhiteTarget(findHome(3, 1));
                                        }
                                    }
                                }
                            }
                            if (kingRook2 && whiteRook2.isAlive()) {
                                if (whiteRook2.getMove() == 0) {
                                    if (safeMoveWhite(whiteKing, findHome(6, 1))) {
                                        if (safeMoveWhite(whiteKing, findHome(7, 1))) {
                                            addWhiteTarget(findHome(7, 1));
                                        }
                                    }
                                }
                            }
                        }
                    } catch (NoHomeException e) {
                        throw new RuntimeException(e);
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteQueen.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteQueen;
                int x = whiteQueen.getHome().getRow();
                int y = whiteQueen.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whiteQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whiteQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whiteQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whiteQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveWhite(whiteQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveWhite(whiteQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveWhite(whiteQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveWhite(whiteQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteBishop1.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteBishop1;
                int x = whiteBishop1.getHome().getRow();
                int y = whiteBishop1.getHome().getColumn();
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveWhite(whiteBishop1, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveWhite(whiteBishop1, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveWhite(whiteBishop1, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveWhite(whiteBishop1, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteBishop2.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteBishop2;
                int x = whiteBishop2.getHome().getRow();
                int y = whiteBishop2.getHome().getColumn();
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveWhite(whiteBishop2, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveWhite(whiteBishop2, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveWhite(whiteBishop2, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveWhite(whiteBishop2, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteKnight1.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteKnight1;
                for (Home home : homes) {
                    int x = abs(home.getRow() - whiteKnight1.getHome().getRow());
                    int y = abs(home.getColumn() - whiteKnight1.getHome().getColumn());
                    if (x != 0 && y != 0 && x + y == 3) {
                        if (safeMoveWhite(whiteKnight1, home)) {
                            addWhiteTarget(home);
                        }
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteKnight2.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteKnight2;
                for (Home home : homes) {
                    int x = abs(home.getRow() - whiteKnight2.getHome().getRow());
                    int y = abs(home.getColumn() - whiteKnight2.getHome().getColumn());
                    if (x != 0 && y != 0 && x + y == 3) {
                        if (safeMoveWhite(whiteKnight2, home)) {
                            addWhiteTarget(home);
                        }
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteRook1.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteRook1;
                int x = whiteRook1.getHome().getRow();
                int y = whiteRook1.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whiteRook1, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whiteRook1, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whiteRook1, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whiteRook1, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
        whiteRook2.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whiteRook2;
                int x = whiteRook2.getHome().getRow();
                int y = whiteRook2.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whiteRook2, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whiteRook2, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whiteRook2, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whiteRook2, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
    }
    private void creatWhitePawns() {
        Image pawnImage = new Image(Chess.class.getResource("image/pawn-white.png").toString());
        for (int i = 1; i <= 8; i++) {
            Pawn pawn = new Pawn(pawnImage, Color.White);
            pane.getChildren().add(pawn);
            whiteMaterials.add(pawn);
            try {
                picking(pawn, findHome(i, 2));
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
            pawn.setOnMouseClicked(event -> {
                if (tern.equals(Tern.White) && onGame) {
                    targets.clear();
                    chosen = pawn;
                    int x = pawn.getHome().getRow();
                    int y = pawn.getHome().getColumn();
                    if (pawn.getMove() == 0) {
                        try {
                            for (int p = 1 ; p <= 2 ; p++) {
                                if (findHome(x, y + p).getMaterial() == null) {
                                    if (safeMoveWhite(pawn, findHome(x, y + p))) {
                                        addWhiteTarget(findHome(x, y + p));
                                    }
                                }
                                if (checkTarget(findHome(x, y + p))) {
                                    break;
                                }
                            }
                            Home home;
                            if (x + 1 <= 8) {
                                home = findHome(x + 1, y + 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.Black)) {
                                        if (safeMoveWhite(pawn, home)) {
                                            addWhiteTarget(home);
                                        }
                                    }
                                }
                            }
                            if (x - 1 >= 1) {
                                home = findHome(x - 1, y + 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.Black)) {
                                        if (safeMoveWhite(pawn, home)) {
                                            addWhiteTarget(home);
                                        }
                                    }
                                }
                            }
                        } catch (NoHomeException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            if (findHome(x, y + 1).getMaterial() == null) {
                                if (safeMoveWhite(pawn, findHome(x, y + 1))) {
                                    addWhiteTarget(findHome(x, y + 1));
                                }
                            }
                            Home home;
                            if (x + 1 <= 8) {
                                home = findHome(x + 1, y + 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.Black)) {
                                        if (safeMoveWhite(pawn, home)) {
                                            addWhiteTarget(home);
                                        }
                                    }
                                }
                            }
                            if (x - 1 >= 1) {
                                home = findHome(x - 1, y + 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.Black)) {
                                        if (safeMoveWhite(pawn, home)) {
                                            addWhiteTarget(home);
                                        }
                                    }
                                }
                            }
                            if (y == 5) {
                                if (x + 1 <= 8) {
                                    home = findHome(x + 1, y);
                                    if (home.getMaterial() != null) {
                                        if (home.getMaterial().getType().equals(Type.Pawn)) {
                                            Pawn tempPawn = (Pawn) home.getMaterial();
                                            if (tempPawn.getColor().equals(Color.Black)) {
                                                if (tempPawn.isEnPassant() && blackEnPassant) {
                                                    if (safeMoveWhite(pawn, findHome(x + 1, y + 1))) {
                                                        addWhiteTarget(findHome(x + 1, y + 1));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (x - 1 >= 1) {
                                    home = findHome(x - 1, y);
                                    if (home.getMaterial() != null) {
                                        if (home.getMaterial().getType().equals(Type.Pawn)) {
                                            Pawn tempPawn = (Pawn) home.getMaterial();
                                            if (tempPawn.getColor().equals(Color.Black)) {
                                                if (tempPawn.isEnPassant() && blackEnPassant) {
                                                    if (safeMoveWhite(pawn, findHome(x - 1, y + 1))) {
                                                        addWhiteTarget(findHome(x - 1, y + 1));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (NoHomeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    showWhiteTargetHomes();
                } else {
                    showNoTern();
                }
            });
        }
    }
    private void showWhiteTargetHomes() {
        for (Target target : targetList) {
            target.setVisible(false);
        }
        targetList.clear();
        for (Home home : targets) {
            Target target = new Target(home);
            targetList.add(target);
            pane.getChildren().add(target);
            target.setOnMouseClicked(event -> {
                whiteMoving(target);
                cancelWhite.setVisible(false);
            });
        }
        cancelWhite.setVisible(true);
    }
    private void addWhiteTarget(Home home) {
        if (home.getMaterial() != null) {
            if (!home.getMaterial().getColor().equals(Color.White)) {
                targets.add(home);
            }
        } else {
            targets.add(home);
        }
    }
    private void whiteMoving(Target target) {
        boolean pawnWinner = false ;
        if (tern.equals(Tern.White) && onGame) {
            if (chosen != null) {
                picking(chosen, target.getHome());
                chosen.setMove(chosen.getMove() + 1);
                changeTern(Tern.Black);
//            for king to rook
                if (chosen.getType().equals(Type.King)) {
                    if (chosen.getMove() == 1) {
                        try {
                            if (target.getHome().getRow() == 7 && target.getHome().getColumn() == 1) {
                                picking(whiteRook2, findHome(6, 1));
                            }
                            if (target.getHome().getRow() == 3 && target.getHome().getColumn() == 1) {
                                picking(whiteRook1, findHome(4, 1));
                            }
                        } catch (NoHomeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
//            for making a pawn to other
                if (chosen.getType().equals(Type.Pawn)) {
                    if (chosen.getHome().getColumn() == 8) {
                        pawnWinner = true ;
                        winnerWhitePawn(chosen);
                    }
                }
//            for en passant
                if (whiteEnPassant) {
                    whiteEnPassant = false;
                    for (Material material : whiteMaterials) {
                        if (material.getType().equals(Type.Pawn)) {
                            Pawn pawn = (Pawn) material;
                            pawn.setEnPassant(false);
                        }
                    }
                }
                if (chosen.getType().equals(Type.Pawn)) {
                    Pawn tempPawn = (Pawn) chosen;
                    if (tempPawn.getHome().getColumn() == 4) {
                        if (tempPawn.getMove() == 1) {
                            tempPawn.setEnPassant(true);
                            whiteEnPassant = true;
                        }
                    }
                    if (tempPawn.getHome().getColumn() == 6) {
                        if (blackEnPassant) {
                            try {
                                Material material = findHome(tempPawn.getHome().getRow(), 5).getMaterial();
                                if (material != null) {
                                    if (material.getType().equals(Type.Pawn)) {
                                        Pawn pawn = (Pawn) material;
                                        if (pawn.isEnPassant()) {
                                            destroyMaterial(material);
                                        }
                                    }
                                }
                            } catch (NoHomeException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
//            clear other targets
                chosen = null;
                for (Target temp : targetList) {
                    temp.setVisible(false);
                }
                targetList.clear();
                targets.clear();
                if (!pawnWinner) {
                    checkMateBlack();
                    goBlackMove();
                }
            }
        }
    }
    private void winnerWhitePawn(Material pawn) {
        Pane pawnPane = new Pane();
        pawnPane.setLayoutX(pawn.getX() - 50);
        pawnPane.setLayoutY(pawn.getY() + 20);
        pawnPane.setPrefWidth(150);
        pawnPane.setPrefHeight(40);
        ImageView backGround = new ImageView(new Image(Chess.class.getResource("image/back-ground.png").toString()));
        pawnPane.getChildren().add(backGround);
        backGround.setFitWidth(150);
        backGround.setFitHeight(40);
        backGround.setOpacity(0.8);
        ImageView queenTemp = new ImageView(new Image(Chess.class.getResource("image/queen-white.png").toString()));
        ImageView rookTemp = new ImageView(new Image(Chess.class.getResource("image/rook-white.png").toString()));
        ImageView bishopTemp = new ImageView(new Image(Chess.class.getResource("image/bishop-white.png").toString()));
        ImageView knightTemp = new ImageView(new Image(Chess.class.getResource("image/knight-white.png").toString()));
        pawnPane.getChildren().add(queenTemp);
        pawnPane.getChildren().add(rookTemp);
        pawnPane.getChildren().add(bishopTemp);
        pawnPane.getChildren().add(knightTemp);
        queenTemp.setX(5);
        queenTemp.setY(5);
        queenTemp.setFitHeight(30);
        queenTemp.setFitWidth(30);
        rookTemp.setX(40);
        rookTemp.setY(5);
        rookTemp.setFitHeight(30);
        rookTemp.setFitWidth(30);
        bishopTemp.setX(75);
        bishopTemp.setY(5);
        bishopTemp.setFitHeight(30);
        bishopTemp.setFitWidth(30);
        knightTemp.setX(110);
        knightTemp.setY(5);
        knightTemp.setFitHeight(30);
        knightTemp.setFitWidth(30);
        pane.getChildren().add(pawnPane);
        tern = Tern.Wait;
        queenTemp.setOnMouseClicked(event -> {
            makeWhitePawnToQueen(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            checkMateBlack();
            goBlackMove();
        });
        rookTemp.setOnMouseClicked(event -> {
            makeWhitePawnToRook(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            checkMateBlack();
            goBlackMove();
        });
        bishopTemp.setOnMouseClicked(event -> {
            makeWhitePawnToBishop(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            checkMateBlack();
            goBlackMove();
        });
        knightTemp.setOnMouseClicked(event -> {
            makeWhitePawnToKnight(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            checkMateBlack();
            goBlackMove();
        });
    }
    private void makeWhitePawnToQueen(Material pawn) {
        Image whiteQueenImage = new Image(Chess.class.getResource("image/queen-white.png").toString());
        Queen whitePawnQueen = new Queen(whiteQueenImage, Color.White);
        pane.getChildren().add(whitePawnQueen);
        whiteMaterials.add(whitePawnQueen);
        try {
            picking(whitePawnQueen, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        whitePawnQueen.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whitePawnQueen;
                int x = whitePawnQueen.getHome().getRow();
                int y = whitePawnQueen.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whitePawnQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whitePawnQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whitePawnQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whitePawnQueen, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveWhite(whitePawnQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveWhite(whitePawnQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveWhite(whitePawnQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveWhite(whitePawnQueen, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
    }
    private void makeWhitePawnToRook(Material pawn) {
        Image whiteRookImage = new Image(Chess.class.getResource("image/rook-white.png").toString());
        Rook whitePawnRook = new Rook(whiteRookImage, Color.White);
        pane.getChildren().add(whitePawnRook);
        whiteMaterials.add(whitePawnRook);
        try {
            picking(whitePawnRook, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        whitePawnRook.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whitePawnRook;
                int x = whitePawnRook.getHome().getRow();
                int y = whitePawnRook.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whitePawnRook, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveWhite(whitePawnRook, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whitePawnRook, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveWhite(whitePawnRook, home)) {
                            addWhiteTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
    }
    private void makeWhitePawnToBishop(Material pawn) {
        Image whiteBishopImage = new Image(Chess.class.getResource("image/bishop-white.png").toString());
        Bishop whitePawnBishop = new Bishop(whiteBishopImage, Color.White);
        pane.getChildren().add(whitePawnBishop);
        whiteMaterials.add(whitePawnBishop);
        try {
            picking(whitePawnBishop, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        whitePawnBishop.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whitePawnBishop;
                int x = whitePawnBishop.getHome().getRow();
                int y = whitePawnBishop.getHome().getColumn();
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveWhite(whitePawnBishop, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveWhite(whitePawnBishop, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveWhite(whitePawnBishop, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveWhite(whitePawnBishop, home)) {
                                addWhiteTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
    }
    private void makeWhitePawnToKnight(Material pawn) {
        Image whiteKnightImage = new Image(Chess.class.getResource("image/knight-white.png").toString());
        Knight whitePawnKnight = new Knight(whiteKnightImage, Color.White);
        pane.getChildren().add(whitePawnKnight);
        whiteMaterials.add(whitePawnKnight);
        try {
            picking(whitePawnKnight, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        whitePawnKnight.setOnMouseClicked(event -> {
            if (tern.equals(Tern.White) && onGame) {
                targets.clear();
                chosen = whitePawnKnight;
                for (Home home : homes) {
                    int x = abs(home.getRow() - whitePawnKnight.getHome().getRow());
                    int y = abs(home.getColumn() - whitePawnKnight.getHome().getColumn());
                    if (x != 0 && y != 0 && x + y == 3) {
                        if (safeMoveWhite(whitePawnKnight, home)) {
                            addWhiteTarget(home);
                        }
                    }
                }
                showWhiteTargetHomes();
            } else {
                showNoTern();
            }
        });
    }
//    check black moves
    private final List<Home> onAttackByWhite = new ArrayList<>();
    private void checkHomesOnAttackByWhite() {
        onAttackByWhite.clear();
        for (Material material : whiteMaterials) {
            switch (material.getType()) {
                case King -> {
                    King king = (King) material;
                    checkWhiteKing(king);
                }
                case Queen -> {
                    Queen queen = (Queen) material;
                    checkWhiteQueen(queen);
                }
                case Bishop -> {
                    Bishop bishop = (Bishop) material;
                    checkWhiteBishop(bishop);
                }
                case Knight -> {
                    Knight knight = (Knight) material;
                    checkWhiteKnight(knight);
                }
                case Rook -> {
                    Rook rook = (Rook) material;
                    checkWhiteRook(rook);
                }
                case Pawn -> {
                    Pawn pawn = (Pawn) material;
                    checkWhitePawn(pawn);
                }
            }
        }
    }
    private void checkWhiteKing(King king) {
        int x = king.getHome().getRow();
        int y = king.getHome().getColumn();
        for (Home home : homes) {
            if (abs(home.getRow() - x) <= 1) {
                if (abs(home.getColumn() - y) <= 1) {
                    addOnAttackByWhite(home);
                }
            }
        }
    }
    private void checkWhiteQueen(Queen queen) {
        int x = queen.getHome().getRow();
        int y = queen.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (queen.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (queen.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (queen.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (queen.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void checkWhiteBishop(Bishop bishop) {
        int x = bishop.getHome().getRow();
        int y = bishop.getHome().getColumn();
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (bishop.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (bishop.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (bishop.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (bishop.isAlive()) {
                        addOnAttackByWhite(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void checkWhiteKnight(Knight knight) {
        for (Home home : homes) {
            int x = abs(home.getRow() - knight.getHome().getRow());
            int y = abs(home.getColumn() - knight.getHome().getColumn());
            if (x != 0 && y != 0 && x + y == 3) {
                if (knight.isAlive()) {
                    addOnAttackByWhite(home);
                }
            }
        }
    }
    private void checkWhiteRook(Rook rook) {
        int x = rook.getHome().getRow();
        int y = rook.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    addOnAttackByWhite(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void checkWhitePawn(Pawn pawn) {
        int x = pawn.getHome().getRow();
        int y = pawn.getHome().getColumn();
        try {
            Home home;
            if (x + 1 <= 8) {
                home = findHome(x + 1, y + 1);
                if (pawn.isAlive()) {
                    addOnAttackByWhite(home);
                }
            }
            if (x - 1 >= 1) {
                home = findHome(x - 1, y + 1);
                if (pawn.isAlive()) {
                    addOnAttackByWhite(home);
                }
            }
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
    }
    private void addOnAttackByWhite(Home home) {
        boolean duplicate = false;
        for (Home temp : onAttackByWhite) {
            if (temp.equals(home)) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            onAttackByWhite.add(home);
        }
    }
    private boolean safeMoveWhite(Material material, Home home) {
        Home firstHome = material.getHome();
        Material firstMaterial = null;
        if (home.getMaterial() != null) {
            firstMaterial = home.getMaterial();
        }

        material.setHome(home);
        home.setMaterial(material);
        firstHome.setMaterial(null);
        if (firstMaterial != null) {
            firstMaterial.setAlive(false);
        }

        checkHomesOnAttackByBlack();
        for (Home targetHome : onAttackByBlack) {
            if (whiteKing.getHome().equals(targetHome)) {
                firstHome.setMaterial(material);
                material.setHome(firstHome);
                home.setMaterial(firstMaterial);
                if (firstMaterial != null) {
                    firstMaterial.setHome(home);
                    firstMaterial.setAlive(true);
                }
                return false;
            }
        }

        firstHome.setMaterial(material);
        material.setHome(firstHome);
        home.setMaterial(firstMaterial);
        if (firstMaterial != null) {
            firstMaterial.setHome(home);
            firstMaterial.setAlive(true);
        }
        return true;
    }
//    check black mate
    private void checkMateWhite() {
        safeHomesForMate.clear();
        for (Material material : whiteMaterials) {
            switch (material.getType()) {
                case King -> {
                    King king = (King) material;
                    safeWhiteKingForMate(king);
                }
                case Queen -> {
                    Queen queen = (Queen) material;
                    safeWhiteQueenForMAte(queen);
                }
                case Bishop -> {
                    Bishop bishop = (Bishop) material;
                    safeWhiteBishopForMate(bishop);
                }
                case Knight -> {
                    Knight knight = (Knight) material;
                    safeWhiteKnightForMAte(knight);
                }
                case Rook -> {
                    Rook rook = (Rook) material;
                    safeWhiteRookForMate(rook);
                }
                case Pawn -> {
                    Pawn pawn = (Pawn) material;
                    safeWhitePawnForMate(pawn);
                }
            }
        }
        if (safeHomesForMate.size() == 0) {
            onGame = true ;
            if (isWhiteChecked()) {
                showEndGame(Result.BlackWin);
            } else {
                showEndGame(Result.Equal);
            }
        }
    }
    private void safeWhiteKingForMate(King king) {
        int x = king.getHome().getRow();
        int y = king.getHome().getColumn();
        for (Home home : homes) {
            if (abs(home.getRow() - x) <= 1) {
                if (abs(home.getColumn() - y) <= 1) {
                    if (safeMoveWhite(king , home)) {
                        if (home.getMaterial() != null) {
                            if (!home.getMaterial().getColor().equals(Color.White)) {
                                safeHomesForMate.add(home);
                            }
                        } else {
                            safeHomesForMate.add(home);
                        }
                    }
                }
            }
        }
    }
    private void safeWhiteQueenForMAte(Queen queen) {
        int x = queen.getHome().getRow();
        int y = queen.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    if (safeMoveWhite(queen, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    if (safeMoveWhite(queen, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    if (safeMoveWhite(queen, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    if (safeMoveWhite(queen, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (queen.isAlive()) {
                        if (safeMoveWhite(queen, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (queen.isAlive()) {
                        if (safeMoveWhite(queen, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (queen.isAlive()) {
                        if (safeMoveWhite(queen, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (queen.isAlive()) {
                        if (safeMoveWhite(queen, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void safeWhiteBishopForMate(Bishop bishop) {
        int x = bishop.getHome().getRow();
        int y = bishop.getHome().getColumn();
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (bishop.isAlive()) {
                        if (safeMoveWhite(bishop, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (bishop.isAlive()) {
                        if (safeMoveWhite(bishop, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (bishop.isAlive()) {
                        if (safeMoveWhite(bishop, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (bishop.isAlive()) {
                        if (safeMoveWhite(bishop, home)) {
                            safeHomesForMate.add(home);
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void safeWhiteKnightForMAte(Knight knight) {
        for (Home home : homes) {
            int x = abs(home.getRow() - knight.getHome().getRow());
            int y = abs(home.getColumn() - knight.getHome().getColumn());
            if (x != 0 && y != 0 && x + y == 3) {
                if (knight.isAlive()) {
                    if (safeMoveWhite(knight, home)) {
                        safeHomesForMate.add(home);
                    }
                }
            }
        }
    }
    private void safeWhiteRookForMate(Rook rook) {
        int x = rook.getHome().getRow();
        int y = rook.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    if (safeMoveWhite(rook, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    if (safeMoveWhite(rook, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    if (safeMoveWhite(rook, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    if (safeMoveWhite(rook, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void safeWhitePawnForMate(Pawn pawn) {
        int x = pawn.getHome().getRow();
        int y = pawn.getHome().getColumn();
        Home home;
        if (pawn.getMove() == 0) {
            try {
                for (int p = 1; p <= 2; p++) {
                    home = findHome(x , y + p) ;
                    if (!checkTarget(home)) {
                        if (safeMoveWhite(pawn, home)) {
                            safeHomesForMate.add(home);
                        }
                    } else {
                        break;
                    }
                }
                if (x + 1 <= 8) {
                    home = findHome(x + 1, y + 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.Black)) {
                            if (safeMoveWhite(pawn, home)) {
                                safeHomesForMate.add(home);
                            }
                        }
                    }
                }
                if (x - 1 >= 1) {
                    home = findHome(x - 1, y + 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.Black)) {
                            if (safeMoveWhite(pawn, home)) {
                                safeHomesForMate.add(home);
                            }
                        }
                    }
                }
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                home = findHome(x, y + 1) ;
                if (home.getMaterial() == null) {
                    if (safeMoveWhite(pawn, home)) {
                        safeHomesForMate.add(home);
                    }
                }
                if (x + 1 <= 8) {
                    home = findHome(x + 1, y + 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.Black)) {
                            if (safeMoveWhite(pawn, home)) {
                                safeHomesForMate.add(home);
                            }
                        }
                    }
                }
                if (x - 1 >= 1) {
                    home = findHome(x - 1, y + 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.Black)) {
                            if (safeMoveWhite(pawn, home)) {
                                safeHomesForMate.add(home);
                            }
                        }
                    }
                }
                if (y == 4) {
                    if (x + 1 <= 8) {
                        home = findHome(x + 1, y);
                        if (home.getMaterial() != null) {
                            if (home.getMaterial().getType().equals(Type.Pawn)) {
                                Pawn tempPawn = (Pawn) home.getMaterial();
                                if (tempPawn.getColor().equals(Color.Black)) {
                                    if (tempPawn.isEnPassant()) {
                                        if (safeMoveWhite(pawn, findHome(x + 1, y + 1))) {
                                            safeHomesForMate.add(findHome(x + 1 , y + 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (x - 1 >= 1) {
                        home = findHome(x - 1, y);
                        if (home.getMaterial() != null) {
                            if (home.getMaterial().getType().equals(Type.Pawn)) {
                                Pawn tempPawn = (Pawn) home.getMaterial();
                                if (tempPawn.getColor().equals(Color.Black)) {
                                    if (tempPawn.isEnPassant()) {
                                        if (safeMoveWhite(pawn, findHome(x + 1, y + 1))) {
                                            safeHomesForMate.add(findHome(x + 1 , y + 1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
        }
        showWhiteTargetHomes();
    }
    private boolean isWhiteChecked () {
        for (Home home : onAttackByBlack) {
            if (home.equals(whiteKing.getHome())) {
                return true ;
            }
        }
        return false ;
    }
    private boolean isBlackChecked () {
        for (Home home : onAttackByWhite) {
            if (home.equals(blackKing.getHome())) {
                return true ;
            }
        }
        return false ;
    }
//    -----

    private List <Move> blackMoves = new ArrayList<>() ;
    private void goBlackMove () {
        blackMoves.clear() ;
        checkMateBlack();
        Random random = new Random() ;
        int a ;
        if (safeHomesForMate.size() != 0) {
            a = random.nextInt(0 , blackMoves.size()) ;
            chosen = blackMoves.get(a).getMaterial() ;
            Target target = blackMoves.get(a).getTarget() ;
            blackMoving(target);
        } else {

        }
    }
    private void addBlackMove (Move move) {
        if (move.getTarget().getHome().getMaterial() == null) {
            blackMoves.add(move);
        } else {
            if (move.getTarget().getHome().getMaterial().getColor().equals(Color.White)) {
                blackMoves.add(move);
            }
        }
    }



//    -----
    //    black
    private King blackKing;
    private Queen blackQueen;
    private Bishop blackBishop1;
    private Bishop blackBishop2;
    private Knight blackKnight1;
    private Knight blackKnight2;
    private Rook blackRook1;
    private Rook blackRook2;
    private boolean blackEnPassant;
    private void creatBlackMaterials() {
//        images
        Image blackKingImage = new Image(Chess.class.getResource("image/king-black.png").toString());
        Image blackQueenImage = new Image(Chess.class.getResource("image/queen-black.png").toString());
        Image blackBishopImage = new Image(Chess.class.getResource("image/bishop-black.png").toString());
        Image blackKnightImage = new Image(Chess.class.getResource("image/knight-black.png").toString());
        Image blackRookImage = new Image(Chess.class.getResource("image/rook-black.png").toString());
//        classes
        blackKing = new King(blackKingImage, Color.Black);
        blackQueen = new Queen(blackQueenImage, Color.Black);
        blackBishop1 = new Bishop(blackBishopImage, Color.Black);
        blackBishop2 = new Bishop(blackBishopImage, Color.Black);
        blackKnight1 = new Knight(blackKnightImage, Color.Black);
        blackKnight2 = new Knight(blackKnightImage, Color.Black);
        blackRook1 = new Rook(blackRookImage, Color.Black);
        blackRook2 = new Rook(blackRookImage, Color.Black);
//        add to pane
        pane.getChildren().add(blackKing);
        pane.getChildren().add(blackQueen);
        pane.getChildren().add(blackBishop1);
        pane.getChildren().add(blackBishop2);
        pane.getChildren().add(blackKnight1);
        pane.getChildren().add(blackKnight2);
        pane.getChildren().add(blackRook1);
        pane.getChildren().add(blackRook2);
//        add to list
        blackMaterials.add(blackKing);
        blackMaterials.add(blackQueen);
        blackMaterials.add(blackBishop1);
        blackMaterials.add(blackBishop2);
        blackMaterials.add(blackKnight1);
        blackMaterials.add(blackKnight2);
        blackMaterials.add(blackRook1);
        blackMaterials.add(blackRook2);
//        picking
        try {
            picking(blackRook1, findHome(1, 8));
            picking(blackKnight1, findHome(2, 8));
            picking(blackBishop1, findHome(3, 8));
            picking(blackQueen, findHome(4, 8));
            picking(blackKing, findHome(5, 8));
            picking(blackBishop2, findHome(6, 8));
            picking(blackKnight2, findHome(7, 8));
            picking(blackRook2, findHome(8, 8));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
//        pawns
        creatBlackPawns();
//        functions
        /*
        blackKing.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackKing;
                int x = blackKing.getHome().getRow();
                int y = blackKing.getHome().getColumn();
                for (Home home : homes) {
                    if (abs(home.getRow() - x) <= 1) {
                        if (abs(home.getColumn() - y) <= 1) {
                            if (safeMoveBlack(blackKing, home)) {
                                addBlackTarget(home);
                            }
                        }
                    }
                }
                if (blackKing.getMove() == 0) {
                    boolean kingRook1 = true;
                    boolean kingRook2 = true;
                    for (int i = x - 1; i >= 2; i--) {
                        try {
                            Home home = findHome(i, y);
                            if (checkTarget(home)) {
                                kingRook1 = false;
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    for (int i = x + 1; i <= 7; i++) {
                        try {
                            Home home = findHome(i, y);
                            if (checkTarget(home)) {
                                kingRook2 = false;
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    try {
                        if (kingRook1 && blackRook1.isAlive()) {
                            if (blackRook1.getMove() == 0) {
                                if (safeMoveBlack(blackKing, findHome(4, 8))) {
                                    if (safeMoveBlack(blackKing, findHome(3, 8))) {
                                        addBlackTarget(findHome(3, 8));
                                    }
                                }
                            }
                        }
                        if (kingRook2 && blackRook2.isAlive()) {
                            if (blackRook2.getMove() == 0) {
                                if (safeMoveBlack(blackKing, findHome(7, 8))) {
                                    if (safeMoveBlack(blackKing, findHome(6, 8))) {
                                        addBlackTarget(findHome(7, 8));
                                    }
                                }
                            }
                        }
                    } catch (NoHomeException e) {
                        throw new RuntimeException(e);
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackQueen.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackQueen;
                int x = blackQueen.getHome().getRow();
                int y = blackQueen.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveBlack(blackQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveBlack(blackQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveBlack(blackQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveBlack(blackQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackBishop1.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackBishop1;
                int x = blackBishop1.getHome().getRow();
                int y = blackBishop1.getHome().getColumn();
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveBlack(blackBishop1, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveBlack(blackBishop1, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveBlack(blackBishop1, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveBlack(blackBishop1, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackBishop2.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackBishop2;
                int x = blackBishop2.getHome().getRow();
                int y = blackBishop2.getHome().getColumn();
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveBlack(blackBishop2, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveBlack(blackBishop2, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveBlack(blackBishop2, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveBlack(blackBishop2, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackKnight1.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackKnight1;
                for (Home home : homes) {
                    int x = abs(home.getRow() - blackKnight1.getHome().getRow());
                    int y = abs(home.getColumn() - blackKnight1.getHome().getColumn());
                    if (x != 0 && y != 0 && x + y == 3) {
                        if (safeMoveBlack(blackKnight1, home)) {
                            addBlackTarget(home);
                        }
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackKnight2.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackKnight2;
                for (Home home : homes) {
                    int x = abs(home.getRow() - blackKnight2.getHome().getRow());
                    int y = abs(home.getColumn() - blackKnight2.getHome().getColumn());
                    if (x != 0 && y != 0 && x + y == 3) {
                        if (safeMoveBlack(blackKnight2, home)) {
                            addBlackTarget(home);
                        }
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackRook1.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackRook1;
                int x = blackRook1.getHome().getRow();
                int y = blackRook1.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackRook1, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackRook1, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackRook1, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackRook1, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        blackRook2.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackRook2;
                int x = blackRook2.getHome().getRow();
                int y = blackRook2.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackRook2, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackRook2, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackRook2, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackRook2, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
        */
    }
    private void creatBlackPawns() {
        Image pawnImage = new Image(Chess.class.getResource("image/pawn-black.png").toString());
        for (int i = 1; i <= 8; i++) {
            Pawn pawn = new Pawn(pawnImage, Color.Black);
            pane.getChildren().add(pawn);
            blackMaterials.add(pawn);
            try {
                picking(pawn, findHome(i, 7));
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
            /*
            pawn.setOnMouseClicked(event -> {
                if (tern.equals(Tern.Black) && onGame) {
                    targets.clear();
                    chosen = pawn;
                    int x = pawn.getHome().getRow();
                    int y = pawn.getHome().getColumn();
                    if (pawn.getMove() == 0) {
                        try {
                            for (int p = 1; p <= 2; p++) {
                                if (findHome(x, y - p).getMaterial() == null) {
                                    if (safeMoveBlack(pawn, findHome(x, y - p))) {
                                        addBlackTarget(findHome(x, y - p));
                                    }
                                }
                                if (checkTarget(findHome(x, y - p))) {
                                    break;
                                }
                            }
                            Home home;
                            if (x + 1 <= 8) {
                                home = findHome(x + 1, y - 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.White)) {
                                        if (safeMoveBlack(pawn, home)) {
                                            addBlackTarget(home);
                                        }
                                    }
                                }
                            }
                            if (x - 1 >= 1) {
                                home = findHome(x - 1, y - 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.White)) {
                                        if (safeMoveBlack(pawn, home)) {
                                            addBlackTarget(home);
                                        }
                                    }
                                }
                            }
                        } catch (NoHomeException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            if (findHome(x, y - 1).getMaterial() == null) {
                                if (safeMoveBlack(pawn, findHome(x, y - 1))) {
                                    addBlackTarget(findHome(x, y - 1));
                                }
                            }
                            Home home;
                            if (x + 1 <= 8) {
                                home = findHome(x + 1, y - 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.White)) {
                                        if (safeMoveBlack(pawn, home)) {
                                            addBlackTarget(home);
                                        }
                                    }
                                }
                            }
                            if (x - 1 >= 1) {
                                home = findHome(x - 1, y - 1);
                                if (home.getMaterial() != null) {
                                    if (home.getMaterial().getColor().equals(Color.White)) {
                                        if (safeMoveBlack(pawn, home)) {
                                            addBlackTarget(home);
                                        }
                                    }
                                }
                            }
                            if (y == 4) {
                                if (x + 1 <= 8) {
                                    home = findHome(x + 1, y);
                                    if (home.getMaterial() != null) {
                                        if (home.getMaterial().getType().equals(Type.Pawn)) {
                                            Pawn tempPawn = (Pawn) home.getMaterial();
                                            if (tempPawn.getColor().equals(Color.White)) {
                                                if (tempPawn.isEnPassant()) {
                                                    if (safeMoveBlack(pawn, findHome(x + 1, y - 1))) {
                                                        addBlackTarget(findHome(x + 1, y - 1));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (x - 1 >= 1) {
                                    home = findHome(x - 1, y);
                                    if (home.getMaterial() != null) {
                                        if (home.getMaterial().getType().equals(Type.Pawn)) {
                                            Pawn tempPawn = (Pawn) home.getMaterial();
                                            if (tempPawn.getColor().equals(Color.White)) {
                                                if (tempPawn.isEnPassant()) {
                                                    if (safeMoveBlack(pawn, findHome(x + 1, y - 1))) {
                                                        addBlackTarget(findHome(x - 1, y - 1));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (NoHomeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    showBlackTargetHomes();
                } else {
                    showNoTern();
                }
            });
            */
        }
    }
    private void addBlackTarget(Home home) {
        if (home.getMaterial() != null) {
            if (!home.getMaterial().getColor().equals(Color.Black)) {
                targets.add(home);
            }
        } else {
            targets.add(home);
        }
    }
    private void showBlackTargetHomes() {
        for (Target target : targetList) {
            target.setVisible(false);
        }
        targetList.clear();
        for (Home home : targets) {
            Target target = new Target(home);
            targetList.add(target);
            pane.getChildren().add(target);
            target.setOnMouseClicked(event -> {
                blackMoving(target);
            });
        }
    }
    private void blackMoving(Target target) {
        boolean pawnWinner = false ;
        if (tern.equals(Tern.Black) && onGame) {
            if (chosen != null) {
                picking(chosen, target.getHome());
                chosen.setMove(chosen.getMove() + 1);
                changeTern(Tern.White);
//            for king to rook
                if (chosen.getType().equals(Type.King)) {
                    if (chosen.getMove() == 1) {
                        try {
                            if (target.getHome().getRow() == 7 && target.getHome().getColumn() == 8) {
                                picking(blackRook2, findHome(6, 8));
                            }
                            if (target.getHome().getRow() == 3 && target.getHome().getColumn() == 8) {
                                picking(blackRook1, findHome(4, 8));
                            }
                        } catch (NoHomeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
//            for making a pawn to other
                if (chosen.getType().equals(Type.Pawn)) {
                    if (chosen.getHome().getColumn() == 1) {
                        pawnWinner = true ;
                        winnerBlackPawn(chosen);
                    }
                }
//            for en passant
                if (blackEnPassant) {
                    blackEnPassant = false;
                    for (Material material : blackMaterials) {
                        if (material.getType().equals(Type.Pawn)) {
                            Pawn pawn = (Pawn) material;
                            pawn.setEnPassant(false);
                        }
                    }
                }
                if (chosen.getType().equals(Type.Pawn)) {
                    Pawn tempPawn = (Pawn) chosen;
                    if (tempPawn.getHome().getColumn() == 5) {
                        if (tempPawn.getMove() == 1) {
                            tempPawn.setEnPassant(true);
                            blackEnPassant = true;
                        }
                    }
                    if (tempPawn.getHome().getColumn() == 3) {
                        if (whiteEnPassant) {
                            try {
                                Material material = findHome(tempPawn.getHome().getRow(), 4).getMaterial();
                                if (material != null) {
                                    if (material.getType().equals(Type.Pawn)) {
                                        Pawn pawn = (Pawn) material;
                                        if (pawn.isEnPassant()) {
                                            destroyMaterial(material);
                                        }
                                    }
                                }
                            } catch (NoHomeException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

//            clear other targets
                chosen = null;
                for (Target temp : targetList) {
                    temp.setVisible(false);
                }
                targetList.clear();
                targets.clear();
                if (!pawnWinner) {
                    checkMateWhite();
                }
            }
        }
    }
    private void winnerBlackPawn(Material pawn) {
        Pane pawnPane = new Pane();
        pawnPane.setLayoutX(pawn.getX() - 50);
        pawnPane.setLayoutY(pawn.getY() - 60);
        pawnPane.setPrefWidth(150);
        pawnPane.setPrefHeight(40);
        ImageView backGround = new ImageView(new Image(Chess.class.getResource("image/back-ground.png").toString()));
        pawnPane.getChildren().add(backGround);
        backGround.setFitWidth(150);
        backGround.setFitHeight(40);
        backGround.setOpacity(0.8);
        ImageView queenTemp = new ImageView(new Image(Chess.class.getResource("image/queen-black.png").toString()));
        ImageView rookTemp = new ImageView(new Image(Chess.class.getResource("image/rook-black.png").toString()));
        ImageView bishopTemp = new ImageView(new Image(Chess.class.getResource("image/bishop-black.png").toString()));
        ImageView knightTemp = new ImageView(new Image(Chess.class.getResource("image/knight-black.png").toString()));
        pawnPane.getChildren().add(queenTemp);
        pawnPane.getChildren().add(rookTemp);
        pawnPane.getChildren().add(bishopTemp);
        pawnPane.getChildren().add(knightTemp);
        queenTemp.setX(5);
        queenTemp.setY(5);
        queenTemp.setFitHeight(30);
        queenTemp.setFitWidth(30);
        rookTemp.setX(40);
        rookTemp.setY(5);
        rookTemp.setFitHeight(30);
        rookTemp.setFitWidth(30);
        bishopTemp.setX(75);
        bishopTemp.setY(5);
        bishopTemp.setFitHeight(30);
        bishopTemp.setFitWidth(30);
        knightTemp.setX(110);
        knightTemp.setY(5);
        knightTemp.setFitHeight(30);
        knightTemp.setFitWidth(30);
        pane.getChildren().add(pawnPane);
        tern = Tern.Wait;
        queenTemp.setOnMouseClicked(event -> {
            makeBlackPawnToQueen(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.White);
            cancelWhite.setVisible(false);
            checkMateWhite();
        });
        rookTemp.setOnMouseClicked(event -> {
            makeBlackPawnToRook(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.White);
            cancelWhite.setVisible(false);
            checkMateWhite();
        });
        bishopTemp.setOnMouseClicked(event -> {
            makeBlackPawnToBishop(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.White);
            cancelWhite.setVisible(false);
            checkMateWhite();
        });
        knightTemp.setOnMouseClicked(event -> {
            makeBlackPawnToKnight(pawn);
            destroyMaterial(pawn);
            pawnPane.setVisible(false);
            changeTern(Tern.White);
            cancelWhite.setVisible(false);
            checkMateWhite();
        });
    }
    private void makeBlackPawnToQueen(Material pawn) {
        Image blackQueenImage = new Image(Chess.class.getResource("image/queen-black.png").toString());
        Queen blackPawnQueen = new Queen(blackQueenImage, Color.Black);
        pane.getChildren().add(blackPawnQueen);
        blackMaterials.add(blackPawnQueen);
        try {
            picking(blackPawnQueen, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        /*
        blackPawnQueen.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackPawnQueen;
                int x = blackPawnQueen.getHome().getRow();
                int y = blackPawnQueen.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackPawnQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackPawnQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackPawnQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackPawnQueen, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveBlack(blackPawnQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveBlack(blackPawnQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveBlack(blackPawnQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveBlack(blackPawnQueen, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
         */
    }
    private void makeBlackPawnToRook(Material pawn) {
        Image blackRookImage = new Image(Chess.class.getResource("image/rook-black.png").toString());
        Rook blackPawnRook = new Rook(blackRookImage, Color.Black);
        pane.getChildren().add(blackPawnRook);
        blackMaterials.add(blackPawnRook);
        try {
            picking(blackPawnRook, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        /*
        blackPawnRook.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackPawnRook;
                int x = blackPawnRook.getHome().getRow();
                int y = blackPawnRook.getHome().getColumn();
                for (int i = x - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackPawnRook, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = x + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(i, y);
                        if (safeMoveBlack(blackPawnRook, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y - 1; i >= 1; i--) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackPawnRook, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                for (int i = y + 1; i <= 8; i++) {
                    try {
                        Home home = findHome(x, i);
                        if (safeMoveBlack(blackPawnRook, home)) {
                            addBlackTarget(home);
                        }
                        if (checkTarget(home)) {
                            break;
                        }
                    } catch (NoHomeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
         */
    }
    private void makeBlackPawnToBishop(Material pawn) {
        Image blackBishopImage = new Image(Chess.class.getResource("image/bishop-black.png").toString());
        Bishop blackPawnBishop = new Bishop(blackBishopImage, Color.Black);
        pane.getChildren().add(blackPawnBishop);
        blackMaterials.add(blackPawnBishop);
        try {
            picking(blackPawnBishop, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        /*
        blackPawnBishop.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackPawnBishop;
                int x = blackPawnBishop.getHome().getRow();
                int y = blackPawnBishop.getHome().getColumn();
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y - i >= 1) {
                        try {
                            Home home = findHome(x - i, y - i);
                            if (safeMoveBlack(blackPawnBishop, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x - i >= 1 && y + i <= 8) {
                        try {
                            Home home = findHome(x - i, y + i);
                            if (safeMoveBlack(blackPawnBishop, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y - i >= 1) {
                        try {
                            Home home = findHome(x + i, y - i);
                            if (safeMoveBlack(blackPawnBishop, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                for (int i = 1; i < 8; i++) {
                    if (x + i <= 8 && y + i <= 8) {
                        try {
                            Home home = findHome(x + i, y + i);
                            if (safeMoveBlack(blackPawnBishop, home)) {
                                addBlackTarget(home);
                            }
                            if (checkTarget(home)) {
                                break;
                            }
                        } catch (NoHomeException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        break;
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
         */
    }
    private void makeBlackPawnToKnight(Material pawn) {
        Image blackKnightImage = new Image(Chess.class.getResource("image/knight-black.png").toString());
        Knight blackPawnKnight = new Knight(blackKnightImage, Color.Black);
        pane.getChildren().add(blackPawnKnight);
        blackMaterials.add(blackPawnKnight);
        try {
            picking(blackPawnKnight, findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        /*
        blackPawnKnight.setOnMouseClicked(event -> {
            if (tern.equals(Tern.Black) && onGame) {
                targets.clear();
                chosen = blackPawnKnight;
                for (Home home : homes) {
                    int x = abs(home.getRow() - blackPawnKnight.getHome().getRow());
                    int y = abs(home.getColumn() - blackPawnKnight.getHome().getColumn());
                    if (x != 0 && y != 0 && x + y == 3) {
                        if (safeMoveBlack(blackPawnKnight, home)) {
                            addBlackTarget(home);
                        }
                    }
                }
                showBlackTargetHomes();
            } else {
                showNoTern();
            }
        });
         */
    }
//    check black moves
    private final List<Home> onAttackByBlack = new ArrayList<>();
    private void checkHomesOnAttackByBlack() {
        onAttackByBlack.clear();
        for (Material material : blackMaterials) {
            switch (material.getType()) {
                case King -> {
                    King king = (King) material;
                    checkBlackKing(king);
                }
                case Queen -> {
                    Queen queen = (Queen) material;
                    checkBlackQueen(queen);
                }
                case Bishop -> {
                    Bishop bishop = (Bishop) material;
                    checkBlackBishop(bishop);
                }
                case Knight -> {
                    Knight knight = (Knight) material;
                    checkBlackKnight(knight);
                }
                case Rook -> {
                    Rook rook = (Rook) material;
                    checkBlackRook(rook);
                }
                case Pawn -> {
                    Pawn pawn = (Pawn) material;
                    checkBlackPawn(pawn);
                }
            }
        }
    }
    private void checkBlackKing(King king) {
        int x = king.getHome().getRow();
        int y = king.getHome().getColumn();
        for (Home home : homes) {
            if (abs(home.getRow() - x) <= 1) {
                if (abs(home.getColumn() - y) <= 1) {
                    addOnAttackByBlack(home);
                }
            }
        }
    }
    private void checkBlackQueen(Queen queen) {
        int x = queen.getHome().getRow();
        int y = queen.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (queen.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (queen.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (queen.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (queen.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void checkBlackBishop(Bishop bishop) {
        int x = bishop.getHome().getRow();
        int y = bishop.getHome().getColumn();
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (bishop.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (bishop.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (bishop.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (bishop.isAlive()) {
                        addOnAttackByBlack(home);
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void checkBlackKnight(Knight knight) {
        for (Home home : homes) {
            int x = abs(home.getRow() - knight.getHome().getRow());
            int y = abs(home.getColumn() - knight.getHome().getColumn());
            if (x != 0 && y != 0 && x + y == 3) {
                if (knight.isAlive()) {
                    addOnAttackByBlack(home);
                }
            }
        }
    }
    private void checkBlackRook(Rook rook) {
        int x = rook.getHome().getRow();
        int y = rook.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    addOnAttackByBlack(home);
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void checkBlackPawn(Pawn pawn) {
        int x = pawn.getHome().getRow();
        int y = pawn.getHome().getColumn();
        try {
            Home home;
            if (x + 1 <= 8) {
                home = findHome(x + 1, y + 1);
                if (pawn.isAlive()) {
                    addOnAttackByBlack(home);
                }
            }
            if (x - 1 >= 1) {
                home = findHome(x - 1, y + 1);
                if (pawn.isAlive()) {
                    addOnAttackByBlack(home);
                }
            }
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
    }
    private void addOnAttackByBlack(Home home) {
        boolean duplicate = false;
        for (Home temp : onAttackByBlack) {
            if (temp.equals(home)) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            onAttackByBlack.add(home);
        }
    }
    private boolean safeMoveBlack(Material material, Home home) {
        Home firstHome = material.getHome();
        Material firstMaterial = null;
        if (home.getMaterial() != null) {
            firstMaterial = home.getMaterial();
        }

        material.setHome(home);
        home.setMaterial(material);
        firstHome.setMaterial(null);
        if (firstMaterial != null) {
            firstMaterial.setAlive(false);
        }

        checkHomesOnAttackByWhite();
        for (Home targetHome : onAttackByWhite) {
            if (blackKing.getHome().equals(targetHome)) {
                firstHome.setMaterial(material);
                material.setHome(firstHome);
                home.setMaterial(firstMaterial);
                if (firstMaterial != null) {
                    firstMaterial.setHome(home);
                    firstMaterial.setAlive(true);
                }
                return false;
            }
        }

        firstHome.setMaterial(material);
        material.setHome(firstHome);
        home.setMaterial(firstMaterial);
        if (firstMaterial != null) {
            firstMaterial.setHome(home);
            firstMaterial.setAlive(true);
        }
        return true;
    }
//    check black mate
    private final List<Home> safeHomesForMate = new ArrayList<>();
    private void checkMateBlack() {
        safeHomesForMate.clear();
        for (Material material : blackMaterials) {
            switch (material.getType()) {
                case King -> {
                    King king = (King) material;
                    safeBlackKingForMate(king);
                }
                case Queen -> {
                    Queen queen = (Queen) material;
                    safeBlackQueenForMate(queen);
                }
                case Bishop -> {
                    Bishop bishop = (Bishop) material;
                    safeBlackBishopForMate(bishop);
                }
                case Knight -> {
                    Knight knight = (Knight) material;
                    safeBlackKnightForMate(knight);
                }
                case Rook -> {
                    Rook rook = (Rook) material;
                    safeBlackRookForMate(rook);
                }
                case Pawn -> {
                    Pawn pawn = (Pawn) material;
                    safeBlackPawnForMate(pawn);
                }
            }
        }
        if (safeHomesForMate.size() == 0) {
            onGame = true ;
            if (isBlackChecked()) {
                showEndGame(Result.WhiteWin);
            } else {
                showEndGame(Result.Equal);
            }
        }
    }
    private void safeBlackKingForMate (King king) {
        int x = king.getHome().getRow();
        int y = king.getHome().getColumn();
        for (Home home : homes) {
            if (abs(home.getRow() - x) <= 1) {
                if (abs(home.getColumn() - y) <= 1) {
                    if (safeMoveBlack(king , home)) {
                        if (home.getMaterial() != null) {
                            if (!home.getMaterial().getColor().equals(Color.Black)) {
                                safeHomesForMate.add(home);
                                addBlackMove(new Move(king , new Target(home)));
                            }
                        } else {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(king , new Target(home)));
                        }
                    }
                }
            }
        }
        if (king.getMove() == 0) {
            boolean kingRook1 = true;
            boolean kingRook2 = true;
            for (int i = x - 1; i >= 2; i--) {
                try {
                    Home home = findHome(i, y);
                    if (checkTarget(home)) {
                        kingRook1 = false;
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = x + 1; i <= 7; i++) {
                try {
                    Home home = findHome(i, y);
                    if (checkTarget(home)) {
                        kingRook2 = false;
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            }
            try {
                if (kingRook1 && blackRook1.isAlive()) {
                    if (blackRook1.getMove() == 0) {
                        if (safeMoveBlack(blackKing, findHome(4, 8))) {
                            if (safeMoveBlack(blackKing, findHome(3, 8))) {
                                safeHomesForMate.add(findHome(3, 8));
                                addBlackMove(new Move(king , new Target(findHome(3, 8))));
                            }
                        }
                    }
                }
                if (kingRook2 && blackRook2.isAlive()) {
                    if (blackRook2.getMove() == 0) {
                        if (safeMoveBlack(blackKing, findHome(6, 8))) {
                            if (safeMoveBlack(blackKing, findHome(7, 8))) {
                                safeHomesForMate.add(findHome(7, 8));
                                addBlackMove(new Move(king , new Target(findHome(7, 8))));
                            }
                        }
                    }
                }
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void safeBlackQueenForMate (Queen queen) {
        int x = queen.getHome().getRow();
        int y = queen.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    if (safeMoveBlack(queen, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(queen , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (queen.isAlive()) {
                    if (safeMoveBlack(queen, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(queen , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    if (safeMoveBlack(queen, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(queen , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (queen.isAlive()) {
                    if (safeMoveBlack(queen, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(queen , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (queen.isAlive()) {
                        if (safeMoveBlack(queen, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(queen , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (queen.isAlive()) {
                        if (safeMoveBlack(queen, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(queen , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (queen.isAlive()) {
                        if (safeMoveBlack(queen, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(queen , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (queen.isAlive()) {
                        if (safeMoveBlack(queen, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(queen , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void safeBlackBishopForMate (Bishop bishop) {
        int x = bishop.getHome().getRow();
        int y = bishop.getHome().getColumn();
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y - i >= 1) {
                try {
                    Home home = findHome(x - i, y - i);
                    if (bishop.isAlive()) {
                        if (safeMoveBlack(bishop, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(bishop , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x - i >= 1 && y + i <= 8) {
                try {
                    Home home = findHome(x - i, y + i);
                    if (bishop.isAlive()) {
                        if (safeMoveBlack(bishop, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(bishop , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y - i >= 1) {
                try {
                    Home home = findHome(x + i, y - i);
                    if (bishop.isAlive()) {
                        if (safeMoveBlack(bishop, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(bishop , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (x + i <= 8 && y + i <= 8) {
                try {
                    Home home = findHome(x + i, y + i);
                    if (bishop.isAlive()) {
                        if (safeMoveBlack(bishop, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(bishop , new Target(home)));
                        }
                    }
                    if (checkTarget(home)) {
                        break;
                    }
                } catch (NoHomeException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                break;
            }
        }
    }
    private void safeBlackKnightForMate (Knight knight) {
        for (Home home : homes) {
            int x = abs(home.getRow() - knight.getHome().getRow());
            int y = abs(home.getColumn() - knight.getHome().getColumn());
            if (x != 0 && y != 0 && x + y == 3) {
                if (knight.isAlive()) {
                    if (safeMoveBlack(knight, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(knight , new Target(home)));
                    }
                }
            }
        }
    }
    private void safeBlackRookForMate (Rook rook) {
        int x = rook.getHome().getRow();
        int y = rook.getHome().getColumn();
        for (int i = x - 1; i >= 1; i--) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    if (safeMoveBlack(rook, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(rook , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = x + 1; i <= 8; i++) {
            try {
                Home home = findHome(i, y);
                if (rook.isAlive()) {
                    if (safeMoveBlack(rook, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(rook , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y - 1; i >= 1; i--) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    if (safeMoveBlack(rook, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(rook , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
        for (int i = y + 1; i <= 8; i++) {
            try {
                Home home = findHome(x, i);
                if (rook.isAlive()) {
                    if (safeMoveBlack(rook, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(rook , new Target(home)));
                    }
                }
                if (checkTarget(home)) {
                    break;
                }
            } catch (NoHomeException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private void safeBlackPawnForMate (Pawn pawn) {
        int x = pawn.getHome().getRow();
        int y = pawn.getHome().getColumn();
        Home home;
        if (pawn.getMove() == 0) {
            try {
                for (int p = 1; p <= 2; p++) {
                    home = findHome(x , y - p) ;
                    if (!checkTarget(home)) {
                        if (safeMoveBlack(pawn, home)) {
                            safeHomesForMate.add(home);
                            addBlackMove(new Move(pawn , new Target(home)));
                        }
                    } else {
                        break;
                    }
                }
                if (x + 1 <= 8) {
                    home = findHome(x + 1, y - 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.White)) {
                            if (safeMoveBlack(pawn, home)) {
                                safeHomesForMate.add(home);
                                addBlackMove(new Move(pawn , new Target(home)));
                            }
                        }
                    }
                }
                if (x - 1 >= 1) {
                    home = findHome(x - 1, y - 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.White)) {
                            if (safeMoveBlack(pawn, home)) {
                                safeHomesForMate.add(home);
                                addBlackMove(new Move(pawn , new Target(home)));
                            }
                        }
                    }
                }
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                home = findHome(x, y - 1) ;
                if (home.getMaterial() == null) {
                    if (safeMoveBlack(pawn, home)) {
                        safeHomesForMate.add(home);
                        addBlackMove(new Move(pawn , new Target(home)));
                    }
                }
                if (x + 1 <= 8) {
                    home = findHome(x + 1, y - 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.White)) {
                            if (safeMoveBlack(pawn, home)) {
                                safeHomesForMate.add(home);
                                addBlackMove(new Move(pawn , new Target(home)));
                            }
                        }
                    }
                }
                if (x - 1 >= 1) {
                    home = findHome(x - 1, y - 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.White)) {
                            if (safeMoveBlack(pawn, home)) {
                                safeHomesForMate.add(home);
                                addBlackMove(new Move(pawn , new Target(home)));
                            }
                        }
                    }
                }
                if (y == 4) {
                    if (x + 1 <= 8) {
                        home = findHome(x + 1, y);
                        if (home.getMaterial() != null) {
                            if (home.getMaterial().getType().equals(Type.Pawn)) {
                                Pawn tempPawn = (Pawn) home.getMaterial();
                                if (tempPawn.getColor().equals(Color.White)) {
                                    if (tempPawn.isEnPassant()) {
                                        if (safeMoveBlack(pawn, findHome(x + 1, y - 1))) {
                                            safeHomesForMate.add(findHome(x + 1 , y - 1));
                                            addBlackMove(new Move(pawn , new Target(findHome(x + 1 , y - 1))));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (x - 1 >= 1) {
                        home = findHome(x - 1, y);
                        if (home.getMaterial() != null) {
                            if (home.getMaterial().getType().equals(Type.Pawn)) {
                                Pawn tempPawn = (Pawn) home.getMaterial();
                                if (tempPawn.getColor().equals(Color.White)) {
                                    if (tempPawn.isEnPassant()) {
                                        if (safeMoveBlack(pawn, findHome(x - 1, y - 1))) {
                                            safeHomesForMate.add(findHome(x - 1 , y - 1));
                                            addBlackMove(new Move(pawn , new Target(findHome(x - 1 , y - 1))));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NoHomeException e) {
                throw new RuntimeException(e);
            }
        }
        showBlackTargetHomes();
    }
}