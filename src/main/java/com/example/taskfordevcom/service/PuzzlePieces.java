package com.example.taskfordevcom.service;

import com.example.taskfordevcom.domain.Desk;
import com.example.taskfordevcom.domain.piece.Piece;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class PuzzlePieces extends Application {
    private Timeline timeline;
    private Desk desk;
    final List<Piece> pieces = new ArrayList<>();
    private static final String PICTURE_URL = "src/main/resources/images/image.png";
    public void init(Stage primaryStage) {

        HBox buttonBox = new HBox(8);
        StackPane deskPane = new StackPane();
        VBox root = new VBox();

        primaryStage.setScene(new Scene(root));

        Image image;
        try {
            image = new Image(new FileInputStream(PICTURE_URL));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int numOfColumns = (int) (image.getWidth() / Piece.SIZE);
        int numOfRows = (int) (image.getHeight() / Piece.SIZE);

        desk = new Desk(numOfColumns, numOfRows);
        buildPieces(numOfColumns, numOfRows, image);
        desk.getChildren().addAll(pieces);

        buttonBox.getChildren().addAll(shuffleButton(), solveButton(), chooseImageButton(primaryStage));

        deskPane.getChildren().add(desk);
        deskPane.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        StackPane.setAlignment(desk, Pos.CENTER);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        root.getChildren().addAll(deskPane, buttonBox);

        VBox.setVgrow(deskPane, Priority.ALWAYS);

        primaryStage.setTitle("Puzzle Pieces");
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
    }

    public void buildPieces(double numOfColumns, double numOfRows, Image image) {
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = Piece.builder()
                        .image(image)
                        .correctX(x)
                        .correctY(y)
                        .hasTopTab(row > 0)
                        .hasLeftTab(col > 0)
                        .hasBottomTab(row < numOfRows - 1)
                        .hasRightTab(col < numOfColumns - 1)
                        .deskWidth(desk.getWidth())
                        .deskHeight(desk.getHeight())
                        .build();
                pieces.add(piece);
            }
        }
    }

    public Button chooseImageButton(Stage primaryStage) {
        Button chooseImageButton = new Button("Choose Image");
        chooseImageButton.setStyle("-fx-font-size: 2em; -fx-background-color: #830101; " +
                                   "-fx-text-fill: #ffffff; -fx-border-color: #000000; " +
                                   "-fx-border-width: 2px; -fx-padding: 5px 10px; " +
                                   "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 );");
        chooseImageButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Виберіть картинку");
            fileChooser.setInitialDirectory(new File("src/main/resources/images/"));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                try {
                    Image newImage;
                    newImage = new Image(new FileInputStream(selectedFile), 1400, 700, false, false);
                    desk.getChildren().clear(); // Clear the previous pieces
                    pieces.clear();
                    int numOfColumns1 = (int) (newImage.getWidth() / Piece.SIZE);
                    int numOfRows1 = (int) (newImage.getHeight() / Piece.SIZE);
                    desk.setNumColumns(numOfColumns1);
                    desk.setNumRows(numOfRows1);
                    buildPieces(numOfColumns1, numOfRows1, newImage);
                    desk.getChildren().addAll(pieces);
                    desk.drawGrid(); // Оновити сітку
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return chooseImageButton;
    }

    public Button solveButton() {
        Button solveButton = new Button("Solve");
        solveButton.setStyle("-fx-font-size: 2em; -fx-background-color: #830101; " +
                             "-fx-text-fill: #ffffff; -fx-border-color: #000000; " +
                             "-fx-border-width: 2px; -fx-padding: 5px 10px;" +
                             "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 );");
        solveButton.setOnAction(actionEvent -> {
            if (timeline != null) timeline.stop();
            timeline = new Timeline();
            for (final Piece piece : pieces) {
                piece.setInactive();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(piece.translateXProperty(), 0),
                                new KeyValue(piece.translateYProperty(), 0)));
            }
            timeline.playFromStart();
        });
        return solveButton;
    }

    public Button shuffleButton() {
        Button shuffleButton = new Button("Shuffle");
        shuffleButton.setStyle("-fx-font-size: 2em; -fx-background-color: #830101; " +
                               "-fx-text-fill: #ffffff; -fx-border-color: #000000; " +
                               "-fx-border-width: 2px; -fx-padding: 5px 10px;" +
                               "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.7) , 10,0,0,1 );");
        shuffleButton.setOnAction(actionEvent -> {
            if (timeline != null) timeline.stop();
            timeline = new Timeline();
            for (final Piece piece : pieces) {
                piece.setActive();
                double shuffleX = Math.random()
                        * (desk.getWidth() - Piece.SIZE + 48f)
                        - 24f - piece.getCorrectX();
                double shuffleY = Math.random()
                        * (desk.getHeight() - Piece.SIZE + 30f)
                        - 15f - piece.getCorrectY();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(1),
                                new KeyValue(piece.translateXProperty(), shuffleX),
                                new KeyValue(piece.translateYProperty(), shuffleY)));
            }
            timeline.playFromStart();
        });
        return shuffleButton;
    }
}

