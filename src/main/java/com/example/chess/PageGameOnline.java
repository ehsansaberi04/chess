package com.example.chess;

import com.example.chess.Exceptions.NoHomeException;
import com.example.chess.Model.* ;
import com.example.chess.Model.Enums.* ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.* ;
import javafx.scene.layout.* ;
import javafx.scene.text.* ;
import javafx.stage.Stage;
import java.io.* ;
import java.net.* ;
import java.util.* ;

import static java.lang.Math.abs;

public class PageGameOnline implements Initializable {
    @FXML
    private AnchorPane pane;
    //    initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        creatHomes();
        firstPage();
    }
//    stage
    private static Stage stage ;
    public static void setStage(Stage stage) {
        PageGameOnline.stage = stage;
    }
    //    connecting
    private void firstPage() {
        Pane firstPane = new Pane();
        pane.getChildren().add(firstPane);
        firstPane.setPrefWidth(400);
        firstPane.setPrefHeight(250);
        firstPane.setLayoutX(100);
        firstPane.setLayoutY(75);
        ImageView imageView = new ImageView(new Image(Chess.class.getResource("image/back-end.png").toString()));
        firstPane.getChildren().add(imageView);
        imageView.setOpacity(0.9);
        Text text = new Text("choose your order");
        firstPane.getChildren().add(text);
        text.setFont(new Font(20));
        text.setStrokeWidth(300);
        text.setX(50);
        text.setY(20);
        Button admin = new Button("admin");
        Button user = new Button("user");
        firstPane.getChildren().add(admin);
        firstPane.getChildren().add(user);
        admin.setPrefWidth(90);
        admin.setPrefHeight(30);
        user.setPrefWidth(90);
        user.setPrefHeight(30);
        admin.setLayoutX(100);
        admin.setLayoutY(180);
        user.setLayoutX(210);
        user.setLayoutY(180);
        admin.setOnMouseClicked(event -> {
            adminConnectPage();
            firstPane.setVisible(false);
        });
        user.setOnMouseClicked(event -> {
            userConnectPage();
            firstPane.setVisible(false);
        });
    }
    private void adminConnectPage() {
        Pane connectPane = new Pane();
        pane.getChildren().add(connectPane);
        connectPane.setPrefWidth(400);
        connectPane.setPrefHeight(250);
        connectPane.setLayoutX(100);
        connectPane.setLayoutY(75);
        ImageView imageView = new ImageView(new Image(Chess.class.getResource("image/back-end.png").toString()));
        connectPane.getChildren().add(imageView);
        imageView.setOpacity(0.9);
        Text text = new Text("enter your port");
        connectPane.getChildren().add(text);
        text.setFont(new Font(20));
        text.setStrokeWidth(300);
        text.setX(50);
        text.setY(20);
        Text textException = new Text("");
        connectPane.getChildren().add(textException);
        textException.setStrokeWidth(200);
        textException.setX(100);
        textException.setY(225);
        TextField portField = new TextField();
        connectPane.getChildren().add(portField);
        portField.setPromptText("port");
        portField.setPrefWidth(200);
        portField.setPrefHeight(40);
        portField.setLayoutX(100);
        portField.setLayoutY(120);
        Button confirm = new Button("confirm");
        Button cancel = new Button("cancel");
        connectPane.getChildren().add(confirm);
        connectPane.getChildren().add(cancel);
        confirm.setPrefWidth(90);
        confirm.setPrefHeight(30);
        cancel.setPrefWidth(90);
        cancel.setPrefHeight(30);
        confirm.setLayoutX(100);
        confirm.setLayoutY(180);
        cancel.setLayoutX(210);
        cancel.setLayoutY(180);
        confirm.setOnMouseClicked(event -> {
            int port = Integer.parseInt(portField.getText());
            try {
                ServerSocket server = new ServerSocket(port);
                textException.setText("Waiting for connection");
                socket = server.accept();
                creatSocketStuffs();
                myColor = Color.White;
                connectPane.setVisible(false);
                startGame();
            } catch (IOException e) {
                textException.setText("port is incorrect");
            }
        });
        cancel.setOnMouseClicked(event -> {
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
    private void userConnectPage() {
        Pane connectPane = new Pane();
        pane.getChildren().add(connectPane);
        connectPane.setPrefWidth(400);
        connectPane.setPrefHeight(250);
        connectPane.setLayoutX(100);
        connectPane.setLayoutY(75);
        ImageView imageView = new ImageView(new Image(Chess.class.getResource("image/back-end.png").toString()));
        connectPane.getChildren().add(imageView);
        imageView.setOpacity(0.9);
        Text text = new Text("enter your address and your port");
        connectPane.getChildren().add(text);
        text.setFont(new Font(20));
        text.setStrokeWidth(300);
        text.setX(50);
        text.setY(20);
        Text textException = new Text("");
        connectPane.getChildren().add(textException);
        textException.setStrokeWidth(200);
        textException.setX(100);
        textException.setY(225);
        TextField addressField = new TextField();
        TextField portField = new TextField();
        connectPane.getChildren().add(addressField);
        connectPane.getChildren().add(portField);
        addressField.setPromptText("address");
        portField.setPromptText("port");
        addressField.setPrefWidth(200);
        addressField.setPrefHeight(40);
        portField.setPrefWidth(200);
        portField.setPrefHeight(40);
        addressField.setLayoutX(100);
        addressField.setLayoutY(60);
        portField.setLayoutX(100);
        portField.setLayoutY(120);
        Button confirm = new Button("confirm");
        Button cancel = new Button("cancel");
        connectPane.getChildren().add(confirm);
        connectPane.getChildren().add(cancel);
        confirm.setPrefWidth(90);
        confirm.setPrefHeight(30);
        cancel.setPrefWidth(90);
        cancel.setPrefHeight(30);
        confirm.setLayoutX(100);
        confirm.setLayoutY(180);
        cancel.setLayoutX(210);
        cancel.setLayoutY(180);
        confirm.setOnMouseClicked(event -> {
            String address = addressField.getText();
            int port = Integer.parseInt(portField.getText());
            try {
                socket = new Socket(address, port);
                creatSocketStuffs();
                connectPane.setVisible(false);
                myColor = Color.Black;
                startGame();
                Thread thread = new Thread(this::firstMoveBlack);
                thread.start();
            } catch (IOException e) {
                textException.setText("There is no connection");
            }
        });
        cancel.setOnMouseClicked(event -> {
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
    private Socket socket ;
    private ObjectOutputStream out ;
    private ObjectInputStream  in  ;
    private void creatSocketStuffs() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Color myColor = null ;
    private void startGame () {
        creatWhiteMaterials();
        creatBlackMaterials();
        creatMaterialPawnWinners();
        creatEndPane();
        tern = Tern.White;
        onGame = true ;
        if (myColor.equals(Color.White)) {
            creatOtherButtonsForWhite();
        } else {
            creatOtherButtonsForBlack();
        }
    }
    private void sending (Move move) {
        if (onGame && socket.isConnected()) {
            try {
                out.writeObject(move);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    private Move receiving () {
        if (onGame && socket.isConnected()){
            try {
                return (Move) in.readObject();
            } catch (ClassNotFoundException | IOException e) {
                return null ;
            }
        } else {
            return null ;
        }
    }
    private void firstMoveBlack () {
        Move move = receiving() ;
        if (move != null) {

            for (Material material : whiteMaterials) {
                if (material.getCode() == move.getMaterial().getCode()) {
                    chosen = material ;
                }
            }
            Target target ;
            for (Home home : homes) {
                if (home.getRow() == move.getTarget().getHome().getRow()) {
                    if (home.getColumn() == move.getTarget().getHome().getColumn()) {
                        target = new Target(home) ;
                        whiteMoving(new Move(chosen , target));
                    }
                }
            }
            changeTern(Tern.Black);
        }
    }
    private void goNextTernMoveWhite () {
        boolean pawnWinner = false ;
        Type pawnWinnerType = null ;
        if (myColor.equals(Color.Black)) {
            changeTern(Tern.White);
            if (onGame) {
                Move move = receiving() ;
                if (move != null) {
                    if (move.getPawnWinner() != null) {
                        pawnWinner = true ;
                        switch (move.getPawnWinner()) {
                            case Queen  -> pawnWinnerType = Type.Queen ;
                            case Bishop -> pawnWinnerType = Type.Bishop ;
                            case Knight -> pawnWinnerType = Type.Knight ;
                            case Rook   -> pawnWinnerType = Type.Rook ;
                        }
                    }
                    for (Material material : whiteMaterials) {
                        if (material.getCode() == move.getMaterial().getCode()) {
                            chosen = material ;
                        }
                    }
                    Target target = null;
                    for (Home home : homes) {
                        if (home.getRow() == move.getTarget().getHome().getRow()) {
                            if (home.getColumn() == move.getTarget().getHome().getColumn()) {
                                target = new Target(home) ;
                            }
                        }
                    }
                    if (pawnWinner) {
                        whiteMoving(new Move(chosen , target , pawnWinnerType));
                    } else {
                        whiteMoving(new Move(chosen , target));
                    }
                    changeTern(Tern.Black);
                }
            }
        }
    }
    private void goNextTernMoveBlack () {
        boolean pawnWinner = false ;
        Type pawn = null ;
        if (myColor.equals(Color.White)) {
            changeTern(Tern.Black);
            if (onGame) {
                Move move = receiving() ;
                if (move != null) {
                    if (move.getPawnWinner() != null) {
                        pawnWinner = true ;
                        switch (move.getPawnWinner()) {
                            case Queen -> pawn = Type.Queen ;
                            case Bishop -> pawn = Type.Bishop ;
                            case Knight -> pawn = Type.Knight ;
                            case Rook -> pawn = Type.Rook ;
                        }
                    }
                    for (Material material : blackMaterials) {
                        if (material.getCode() == move.getMaterial().getCode()) {
                            chosen = material ;
                        }
                    }
                    Target target = null ;
                    for (Home home : homes) {
                        if (home.getRow() == move.getTarget().getHome().getRow()) {
                            if (home.getColumn() == move.getTarget().getHome().getColumn()) {
                                target = new Target(home) ;
                            }
                        }
                    }
                    if (pawnWinner) {
                        blackMoving(new Move(chosen , target , pawn));
                    } else {
                        blackMoving(new Move(chosen , target));
                    }
                    changeTern(Tern.White);
                }
            }
        }
    }
//        chess
    private final ImageView cancelWhite = new ImageView(new Image(Chess.class.getResource("image/cancel.png").toString()));
    private final ImageView cancelBlack = new ImageView(new Image(Chess.class.getResource("image/cancel.png").toString()));
    private final Pane endPane = new Pane() ;
    private final Queen[] pawnWinnerQueenTemp = new Queen [16] ;
    private final Bishop[] pawnWinnerBishopTemp = new Bishop [16] ;
    private final Knight[] pawnWinnerKnightTemp = new Knight [16] ;
    private final Rook[] pawnWinnerRookTemp = new Rook [16] ;
//    mains
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
    private int pawnWinnerCounter = 0 ;
    private void creatMaterialPawnWinners () {
        for (int i = 0 ; i < 16 ; i++) {
            pawnWinnerQueenTemp [i]  = new Queen (null , Color.Null);
            pawnWinnerBishopTemp [i] = new Bishop(null , Color.Null);
            pawnWinnerKnightTemp [i] = new Knight(null , Color.Null);
            pawnWinnerRookTemp [i]   = new Rook  (null , Color.Null);
            pane.getChildren().add(pawnWinnerQueenTemp[i]) ;
            pane.getChildren().add(pawnWinnerBishopTemp[i]) ;
            pane.getChildren().add(pawnWinnerKnightTemp[i]) ;
            pane.getChildren().add(pawnWinnerRookTemp[i]) ;
            pawnWinnerQueenTemp[i].setVisible(false);
            pawnWinnerBishopTemp[i].setVisible(false);
            pawnWinnerKnightTemp[i].setVisible(false);
            pawnWinnerRookTemp[i].setVisible(false);
        }
    }
    private void creatEndPane() {
        pane.getChildren().add(endPane) ;
        endPane.setVisible(false);
        endPane.setPrefHeight(400);
        endPane.setPrefWidth(250);
        endPane.setLayoutX(100);
        endPane.setLayoutY(75);
        ImageView back = new ImageView(new Image(Chess.class.getResource("image/back-end.png").toString())) ;
        endPane.getChildren().add(back) ;
        back.setOpacity(0.6);
        endText = new Text();
        endPane.getChildren().add(endText) ;
        endText.setFont(new Font(20));
        endText.setStrokeWidth(100);
        endText.setX(150);
        endText.setY(100);
        endButton = new Button("back");
        endPane.getChildren().add(endButton) ;
        endButton.setPrefWidth(100);
        endButton.setPrefHeight(30);
        endButton.setLayoutX(150);
        endButton.setLayoutY(200);
    }
    private Text endText ;
    private Button endButton ;
    private void creatOtherButtonsForWhite () {
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
    }
    private void creatOtherButtonsForBlack () {
        pane.getChildren().add(cancelBlack);
        cancelBlack.setFitHeight(50);
        cancelBlack.setFitWidth(50);
        cancelBlack.setX(525);
        cancelBlack.setY(25);
        cancelBlack.setVisible(false);
        cancelBlack.setOnMouseClicked(event -> {
            for (Target target : targetList) {
                target.setVisible(false);
            }
            targetList.clear();
            targets.clear();
            chosen = null;
            cancelBlack.setVisible(false);
        });
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
        cancelBlack.setVisible(false);
        endPane.setVisible(true);
        switch (result) {
            case WhiteWin -> {
                if (myColor.equals(Color.White)) {
                    endText.setText("you win");
                } else {
                    endText.setText("you lose");
                }
            }
            case BlackWin -> {
                if (myColor.equals(Color.Black)) {
                    endText.setText("you win");
                } else {
                    endText.setText("you lose");
                }
            }
            case Equal -> endText.setText("The game equalised");
        }
        endButton.setOnMouseClicked(event -> {
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
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            if (myColor.equals(Color.White)) {
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
                                        if (safeMoveWhite(whiteKing, findHome(3, 1))) {
                                            addWhiteTarget(findHome(3, 1));
                                        }
                                    }
                                }
                                if (kingRook2 && whiteRook2.isAlive()) {
                                    if (whiteRook2.getMove() == 0) {
                                        if (safeMoveWhite(whiteKing, findHome(7, 1))) {
                                            addWhiteTarget(findHome(7, 1));
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
            }
        });
        whiteQueen.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
            }
        });
        whiteBishop1.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
            }
        });
        whiteBishop2.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
            }
        });
        whiteKnight1.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
            }
        });
        whiteKnight2.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
            }
        });
        whiteRook1.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
            }
        });
        whiteRook2.setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
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
                if (myColor.equals(Color.White)) {
                    if (tern.equals(Tern.White) && onGame) {
                        targets.clear();
                        chosen = pawn;
                        int x = pawn.getHome().getRow();
                        int y = pawn.getHome().getColumn();
                        if (pawn.getMove() == 0) {
                            try {
                                for (int p = 1; p <= 2; p++) {
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
                }
            });
        }
    }
    private void showWhiteTargetHomes() {
        for (Target target : targetList) {
            target.setVisible(false);
        }
        targetList.clear();
        cancelWhite.setVisible(true);
        for (Home home : targets) {
            Target target = new Target(home);
            targetList.add(target);
            pane.getChildren().add(target);
            target.setOnMouseClicked(event -> {
                whiteMoving(new Move(chosen , target));
                cancelWhite.setVisible(false);
            });
        }
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
    private synchronized void whiteMoving(Move move) {
        Target target = move.getTarget() ;
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
                if (myColor.equals(Color.White)) {
                    if (chosen.getType().equals(Type.Pawn)) {
                        if (chosen.getHome().getColumn() == 8) {
                            pawnWinner = true ;
                            winnerWhitePawn(chosen);
                        }
                    }
                } else {
                    if (chosen.getType().equals(Type.Pawn)) {
                        if (chosen.getHome().getColumn() == 8) {
                            pawnWinner = true;
                            switch (move.getPawnWinner()) {
                                case Queen -> makeWhitePawnToQueen(chosen);
                                case Bishop -> makeWhitePawnToBishop(chosen);
                                case Knight -> makeWhitePawnToKnight(chosen);
                                case Rook -> makeWhitePawnToRook(chosen);
                            }
                        }
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
                if (!pawnWinner) {
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
                }
//            other works
                for (Target temp : targetList) {
                    temp.setVisible(false);
                }
                targetList.clear();
                targets.clear();
                if (! pawnWinner) {
                    if (myColor.equals(Color.White)){
                        sending(new Move(chosen , target));
                        Thread thread = new Thread(this :: goNextTernMoveBlack);
                        thread.start();
                    }
                    checkMateBlack();
                }
            }
        }
    }
    private void winnerWhitePawn(Material material) {
        Pane pawnPane = new Pane();
        pawnPane.setLayoutX(material.getX() - 50);
        pawnPane.setLayoutY(material.getY() + 20);
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
            makeWhitePawnToQueen(material);
            destroyMaterial(material);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            cancelBlack.setVisible(false);
            checkMateBlack();
        });
        rookTemp.setOnMouseClicked(event -> {
            makeWhitePawnToRook(material);
            destroyMaterial(material);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            cancelBlack.setVisible(false);
            checkMateBlack();
        });
        bishopTemp.setOnMouseClicked(event -> {
            makeWhitePawnToBishop(material);
            destroyMaterial(material);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            cancelBlack.setVisible(false);
            checkMateBlack();
        });
        knightTemp.setOnMouseClicked(event -> {
            makeWhitePawnToKnight(material);
            destroyMaterial(material);
            pawnPane.setVisible(false);
            changeTern(Tern.Black);
            cancelBlack.setVisible(false);
            checkMateBlack();
        });
    }
    private int pawnWinnerCode = 150 ;
    private void makeWhitePawnToQueen(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image whiteQueenImage = new Image(Chess.class.getResource("image/queen-white.png").toString());
        if (myColor.equals(Color.White)) {
            pawnWinnerQueenTemp[a] = new Queen(whiteQueenImage, Color.White);
            pane.getChildren().add(pawnWinnerQueenTemp[a]);
        } else {
            pawnWinnerQueenTemp[a].setImage(whiteQueenImage);
            pawnWinnerQueenTemp[a].setColor(Color.White);
            pawnWinnerQueenTemp[a].setType(Type.Queen);
            pawnWinnerQueenTemp[a].setFitWidth(50);
            pawnWinnerQueenTemp[a].setFitHeight(50);
            pawnWinnerQueenTemp[a].setX(pawn.getX());
            pawnWinnerQueenTemp[a].setY(pawn.getY());
            pawnWinnerQueenTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
            pawnWinnerQueenTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        whiteMaterials.add(pawnWinnerQueenTemp[a]);
        System.out.println("i am a queen : " + whiteMaterials.indexOf(pawnWinnerQueenTemp[a]));
        System.out.println("my code is : " + pawnWinnerQueenTemp[a].getCode());
        try {
            picking(pawnWinnerQueenTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateBlack();
        if (myColor.equals(Color.White)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Queen));
            Thread thread = new Thread(this::goNextTernMoveBlack);
            thread.start();
        }
        pawnWinnerQueenTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
                if (tern.equals(Tern.White) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerQueenTemp[a];
                    int x = pawnWinnerQueenTemp[a].getHome().getRow();
                    int y = pawnWinnerQueenTemp[a].getHome().getColumn();
                    for (int i = x - 1; i >= 1; i--) {
                        try {
                            Home home = findHome(i, y);
                            if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                            if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                            if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                            if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerQueenTemp[a], home)) {
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
            }
        });
    }
    private void makeWhitePawnToRook(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image whiteRookImage = new Image(Chess.class.getResource("image/rook-white.png").toString());
        if (myColor.equals(Color.White)) {
            pawnWinnerRookTemp[a] = new Rook(whiteRookImage, Color.White);
            pane.getChildren().add(pawnWinnerRookTemp[a]);
        } else {
            pawnWinnerRookTemp[a].setImage(whiteRookImage);
            pawnWinnerRookTemp[a].setColor(Color.White);
            pawnWinnerRookTemp[a].setType(Type.Rook);
            pawnWinnerRookTemp[a].setFitWidth(50);
            pawnWinnerRookTemp[a].setFitHeight(50);
            pawnWinnerRookTemp[a].setX(pawn.getX());
            pawnWinnerRookTemp[a].setY(pawn.getY());
            pawnWinnerRookTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerRookTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        whiteMaterials.add(pawnWinnerRookTemp[a]);
        try {
            picking(pawnWinnerRookTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateBlack();
        if (myColor.equals(Color.White)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Rook));
            Thread thread = new Thread(this::goNextTernMoveBlack);
            thread.start();
        }
        pawnWinnerRookTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
                if (tern.equals(Tern.White) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerRookTemp[a];
                    int x = pawnWinnerRookTemp[a].getHome().getRow();
                    int y = pawnWinnerRookTemp[a].getHome().getColumn();
                    for (int i = x - 1; i >= 1; i--) {
                        try {
                            Home home = findHome(i, y);
                            if (safeMoveWhite(pawnWinnerRookTemp[a], home)) {
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
                            if (safeMoveWhite(pawnWinnerRookTemp[a], home)) {
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
                            if (safeMoveWhite(pawnWinnerRookTemp[a], home)) {
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
                            if (safeMoveWhite(pawnWinnerRookTemp[a], home)) {
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
            }
        });
    }
    private void makeWhitePawnToBishop(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image whiteBishopImage = new Image(Chess.class.getResource("image/bishop-white.png").toString());
        if (myColor.equals(Color.White)) {
            pawnWinnerBishopTemp[a] = new Bishop(whiteBishopImage, Color.White);
            pane.getChildren().add(pawnWinnerBishopTemp[a]);
        } else {
            pawnWinnerBishopTemp[a].setImage(whiteBishopImage);
            pawnWinnerBishopTemp[a].setColor(Color.White);
            pawnWinnerBishopTemp[a].setType(Type.Bishop);
            pawnWinnerBishopTemp[a].setFitWidth(50);
            pawnWinnerBishopTemp[a].setFitHeight(50);
            pawnWinnerBishopTemp[a].setX(pawn.getX());
            pawnWinnerBishopTemp[a].setY(pawn.getY());
            pawnWinnerBishopTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerBishopTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        whiteMaterials.add(pawnWinnerBishopTemp[a]);
        try {
            picking(pawnWinnerBishopTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateBlack();
        if (myColor.equals(Color.White)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Bishop));
            Thread thread = new Thread(this::goNextTernMoveBlack);
            thread.start();
        }
        pawnWinnerBishopTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
                if (tern.equals(Tern.White) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerBishopTemp[a];
                    int x = pawnWinnerBishopTemp[a].getHome().getRow();
                    int y = pawnWinnerBishopTemp[a].getHome().getColumn();
                    for (int i = 1; i < 8; i++) {
                        if (x - i >= 1 && y - i >= 1) {
                            try {
                                Home home = findHome(x - i, y - i);
                                if (safeMoveWhite(pawnWinnerBishopTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerBishopTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerBishopTemp[a], home)) {
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
                                if (safeMoveWhite(pawnWinnerBishopTemp[a], home)) {
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
            }
        });
    }
    private void makeWhitePawnToKnight(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image whiteKnightImage = new Image(Chess.class.getResource("image/knight-white.png").toString());
        if (myColor.equals(Color.White)) {
            pawnWinnerKnightTemp[a] = new Knight(whiteKnightImage, Color.White);
            pane.getChildren().add(pawnWinnerKnightTemp[a]);
        } else {
            pawnWinnerKnightTemp[a].setImage(whiteKnightImage);
            pawnWinnerKnightTemp[a].setColor(Color.White);
            pawnWinnerKnightTemp[a].setType(Type.Knight);
            pawnWinnerKnightTemp[a].setFitWidth(50);
            pawnWinnerKnightTemp[a].setFitHeight(50);
            pawnWinnerKnightTemp[a].setX(pawn.getX());
            pawnWinnerKnightTemp[a].setY(pawn.getY());
            pawnWinnerKnightTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerKnightTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        whiteMaterials.add(pawnWinnerKnightTemp[a]);
//        -----------------------------------------
        try {
            picking(pawnWinnerKnightTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateBlack();
        if (myColor.equals(Color.White)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Knight));
            Thread thread = new Thread(this::goNextTernMoveBlack);
            thread.start();
        }
        pawnWinnerKnightTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.White)) {
                if (tern.equals(Tern.White) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerKnightTemp[a];
                    for (Home home : homes) {
                        int x = abs(home.getRow() - pawnWinnerKnightTemp[a].getHome().getRow());
                        int y = abs(home.getColumn() - pawnWinnerKnightTemp[a].getHome().getColumn());
                        if (x != 0 && y != 0 && x + y == 3) {
                            if (safeMoveWhite(pawnWinnerKnightTemp[a], home)) {
                                addWhiteTarget(home);
                            }
                        }
                    }
                    showWhiteTargetHomes();
                } else {
                    showNoTern();
                }
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

        blackKing.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
                                    if (safeMoveBlack(blackKnight1, findHome(3, 8))) {
                                        addBlackTarget(findHome(3, 8));
                                    }
                                }
                            }
                            if (kingRook2 && blackRook2.isAlive()) {
                                if (blackRook2.getMove() == 0) {
                                    if (safeMoveBlack(blackKnight1, findHome(7, 8))) {
                                        addBlackTarget(findHome(7, 8));
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
            }
        });
        blackQueen.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
        blackBishop1.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
        blackBishop2.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
        blackKnight1.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
        blackKnight2.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
        blackRook1.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
        blackRook2.setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
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
            }
        });
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
            pawn.setOnMouseClicked(event -> {
                if (myColor.equals(Color.Black)) {
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
                }
            });
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
                blackMoving(new Move(chosen , target));
                cancelBlack.setVisible(false);
            });
        }
        cancelBlack.setVisible(true);
    }
    private void blackMoving(Move move) {
        Target target = move.getTarget() ;
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
                if (myColor.equals(Color.Black)) {
                    if (chosen.getType().equals(Type.Pawn)) {
                        if (chosen.getHome().getColumn() == 1) {
                            pawnWinner = true ;
                            winnerBlackPawn(chosen);
                        }
                    }
                } else if (myColor.equals(Color.White)) {
                    if (chosen.getType().equals(Type.Pawn)) {
                        if (chosen.getHome().getColumn() == 1) {
                            pawnWinner = true ;
                            switch (move.getPawnWinner()) {
                                case Queen -> makeBlackPawnToQueen(chosen);
                                case Bishop -> makeBlackPawnToBishop(chosen);
                                case Knight -> makeBlackPawnToKnight(chosen);
                                case Rook -> makeBlackPawnToRook(chosen);
                            }
                        }
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
//            other works
                for (Target temp : targetList) {
                    temp.setVisible(false);
                }
                targetList.clear();
                targets.clear();
                if (! pawnWinner) {
                    if (myColor.equals(Color.Black)){
                        sending(new Move(chosen , target));
                        Thread thread = new Thread(this::goNextTernMoveWhite);
                        thread.start();
                    }
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
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image blackQueenImage = new Image(Chess.class.getResource("image/queen-black.png").toString());
        if (myColor.equals(Color.Black)) {
            pawnWinnerQueenTemp[a] = new Queen(blackQueenImage, Color.Black);
            pane.getChildren().add(pawnWinnerQueenTemp[a]);
        } else {
            pawnWinnerQueenTemp[a].setImage(blackQueenImage);
            pawnWinnerQueenTemp[a].setColor(Color.Black);
            pawnWinnerQueenTemp[a].setType(Type.Queen);
            pawnWinnerQueenTemp[a].setFitWidth(50);
            pawnWinnerQueenTemp[a].setFitHeight(50);
            pawnWinnerQueenTemp[a].setX(pawn.getX());
            pawnWinnerQueenTemp[a].setY(pawn.getY());
            pawnWinnerQueenTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerQueenTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        blackMaterials.add(pawnWinnerQueenTemp[a]);
        try {
            picking(pawnWinnerQueenTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateWhite();
        if (myColor.equals(Color.Black)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Queen));
            Thread thread = new Thread(this::goNextTernMoveWhite);
            thread.start();
        }
        pawnWinnerQueenTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
                if (tern.equals(Tern.Black) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerQueenTemp[a];
                    int x = pawnWinnerQueenTemp[a].getHome().getRow();
                    int y = pawnWinnerQueenTemp[a].getHome().getColumn();
                    for (int i = x - 1; i >= 1; i--) {
                        try {
                            Home home = findHome(i, y);
                            if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                            if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                            if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                            if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerQueenTemp[a], home)) {
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
            }
        });
    }
    private void makeBlackPawnToRook(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image blackRookImage = new Image(Chess.class.getResource("image/rook-black.png").toString());
        if (myColor.equals(Color.Black)) {
            pawnWinnerRookTemp[a] = new Rook(blackRookImage, Color.Black);
            pane.getChildren().add(pawnWinnerRookTemp[a]);
        } else {
            pawnWinnerRookTemp[a].setImage(blackRookImage);
            pawnWinnerRookTemp[a].setColor(Color.Black);
            pawnWinnerRookTemp[a].setType(Type.Rook);
            pawnWinnerRookTemp[a].setFitWidth(50);
            pawnWinnerRookTemp[a].setFitHeight(50);
            pawnWinnerRookTemp[a].setX(pawn.getX());
            pawnWinnerRookTemp[a].setY(pawn.getY());
            pawnWinnerRookTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerRookTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        blackMaterials.add(pawnWinnerRookTemp[a]);
        try {
            picking(pawnWinnerRookTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        if (myColor.equals(Color.Black)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Rook));
            Thread thread = new Thread(this::goNextTernMoveWhite);
            thread.start();
        }
        checkMateWhite();
        pawnWinnerRookTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
                if (tern.equals(Tern.Black) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerRookTemp[a];
                    int x = pawnWinnerRookTemp[a].getHome().getRow();
                    int y = pawnWinnerRookTemp[a].getHome().getColumn();
                    for (int i = x - 1; i >= 1; i--) {
                        try {
                            Home home = findHome(i, y);
                            if (safeMoveBlack(pawnWinnerRookTemp[a], home)) {
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
                            if (safeMoveBlack(pawnWinnerRookTemp[a], home)) {
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
                            if (safeMoveBlack(pawnWinnerRookTemp[a], home)) {
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
                            if (safeMoveBlack(pawnWinnerRookTemp[a], home)) {
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
            }
        });
    }
    private void makeBlackPawnToBishop(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image blackBishopImage = new Image(Chess.class.getResource("image/bishop-black.png").toString());
        if (myColor.equals(Color.Black)) {
            pawnWinnerBishopTemp[a] = new Bishop(blackBishopImage, Color.Black);
            pane.getChildren().add(pawnWinnerBishopTemp[a]);
        } else {
            pawnWinnerBishopTemp[a].setImage(blackBishopImage);
            pawnWinnerBishopTemp[a].setColor(Color.Black);
            pawnWinnerBishopTemp[a].setType(Type.Bishop);
            pawnWinnerBishopTemp[a].setFitWidth(50);
            pawnWinnerBishopTemp[a].setFitHeight(50);
            pawnWinnerBishopTemp[a].setX(pawn.getX());
            pawnWinnerBishopTemp[a].setY(pawn.getY());
            pawnWinnerBishopTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerBishopTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        blackMaterials.add(pawnWinnerBishopTemp[a]);
        try {
            picking(pawnWinnerBishopTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateWhite();
        if (myColor.equals(Color.Black)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Bishop));
            Thread thread = new Thread(this::goNextTernMoveWhite);
            thread.start();
        }
        pawnWinnerBishopTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
                if (tern.equals(Tern.Black) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerBishopTemp[a];
                    int x = pawnWinnerBishopTemp[a].getHome().getRow();
                    int y = pawnWinnerBishopTemp[a].getHome().getColumn();
                    for (int i = 1; i < 8; i++) {
                        if (x - i >= 1 && y - i >= 1) {
                            try {
                                Home home = findHome(x - i, y - i);
                                if (safeMoveBlack(pawnWinnerBishopTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerBishopTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerBishopTemp[a], home)) {
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
                                if (safeMoveBlack(pawnWinnerBishopTemp[a], home)) {
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
            }
        });
    }
    private void makeBlackPawnToKnight(Material pawn) {
        int a = pawnWinnerCounter ;
        pawnWinnerCounter ++ ;
        Image blackKnightImage = new Image(Chess.class.getResource("image/knight-black.png").toString());
        if (myColor.equals(Color.Black)) {
            pawnWinnerKnightTemp[a] = new Knight(blackKnightImage, Color.Black);
            pane.getChildren().add(pawnWinnerKnightTemp[a]);
        } else {
            pawnWinnerKnightTemp[a].setImage(blackKnightImage);
            pawnWinnerKnightTemp[a].setColor(Color.Black);
            pawnWinnerKnightTemp[a].setType(Type.Knight);
            pawnWinnerKnightTemp[a].setFitWidth(50);
            pawnWinnerKnightTemp[a].setFitHeight(50);
            pawnWinnerKnightTemp[a].setX(pawn.getX());
            pawnWinnerKnightTemp[a].setY(pawn.getY());
            pawnWinnerKnightTemp[a].setVisible(true);
            destroyMaterial(pawn);
        }
        pawnWinnerKnightTemp[a].setCode(pawnWinnerCode) ;
        pawnWinnerCode ++ ;
        blackMaterials.add(pawnWinnerKnightTemp[a]);
        try {
            picking(pawnWinnerKnightTemp[a], findHome(pawn.getHome().getRow(), pawn.getHome().getColumn()));
        } catch (NoHomeException e) {
            throw new RuntimeException(e);
        }
        checkMateWhite();
        if (myColor.equals(Color.Black)){
            sending(new Move(pawn , new Target(pawn.getHome()) , Type.Knight));
            Thread thread = new Thread(this::goNextTernMoveWhite);
            thread.start();
        }
        pawnWinnerKnightTemp[a].setOnMouseClicked(event -> {
            if (myColor.equals(Color.Black)) {
                if (tern.equals(Tern.Black) && onGame) {
                    targets.clear();
                    chosen = pawnWinnerKnightTemp[a];
                    for (Home home : homes) {
                        int x = abs(home.getRow() - pawnWinnerKnightTemp[a].getHome().getRow());
                        int y = abs(home.getColumn() - pawnWinnerKnightTemp[a].getHome().getColumn());
                        if (x != 0 && y != 0 && x + y == 3) {
                            if (safeMoveBlack(pawnWinnerKnightTemp[a], home)) {
                                addBlackTarget(home);
                            }
                        }
                    }
                    showBlackTargetHomes();
                } else {
                    showNoTern();
                }
            }
        });
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
                            }
                        } else {
                            safeHomesForMate.add(home);
                        }
                    }
                }
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
                    }
                }
                if (x + 1 <= 8) {
                    home = findHome(x + 1, y - 1);
                    if (home.getMaterial() != null) {
                        if (home.getMaterial().getColor().equals(Color.White)) {
                            if (safeMoveBlack(pawn, home)) {
                                safeHomesForMate.add(home);
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
                                            safeHomesForMate.add(findHome(x + 1 , y - 1));
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
    private boolean isBlackChecked () {
        for (Home home : onAttackByWhite) {
            if (home.equals(blackKing.getHome())) {
                return true ;
            }
        }
        return false ;
    }
}