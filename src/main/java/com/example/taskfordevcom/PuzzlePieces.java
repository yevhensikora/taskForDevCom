package com.example.taskfordevcom;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
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

    public void init(Stage primaryStage) {

        HBox buttonBox = new HBox(8);
        VBox vBox = new VBox(10);
        Group root = new Group();
        primaryStage.setScene(new Scene(root));

        Image image;
        try {
            image = new Image(new FileInputStream("src/main/resources/images/image.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int numOfColumns = (int) (image.getWidth() / Piece.SIZE);
        int numOfRows = (int) (image.getHeight() / Piece.SIZE);

        desk = new Desk(numOfColumns, numOfRows);
        buildPieces(numOfColumns, numOfRows, image);
        desk.getChildren().addAll(pieces);

        buttonBox.getChildren().addAll(shuffleButton(), solveButton(), chooseImageButton(primaryStage));

        vBox.getChildren().addAll(desk, buttonBox);
        root.getChildren().addAll(vBox);
    }

    @Override
    public void start(Stage primaryStage) {
        init(primaryStage);
        primaryStage.show();
    }

    public void buildPieces(double numOfColumns, double numOfRows, Image image) {
        for (int col = 0; col < numOfColumns; col++) {
            for (int row = 0; row < numOfRows; row++) {
                int x = col * Piece.SIZE;
                int y = row * Piece.SIZE;
                final Piece piece = new Piece(image, x, y, row > 0, col > 0,
                        row < numOfRows - 1, col < numOfColumns - 1,
                        desk.getWidth(), desk.getHeight());
                pieces.add(piece);
            }
        }
    }

    public Button chooseImageButton(Stage primaryStage) {
        Button chooseImageButton = new Button("Choose Image");
        chooseImageButton.setStyle("-fx-font-size: 2em");

        chooseImageButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Виберіть картинку");
            fileChooser.setInitialDirectory(new File("src/main/resources/images/"));
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                try {
                    Image newImage = null;
                    newImage = new Image(new FileInputStream(selectedFile));
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
        solveButton.setStyle("-fx-font-size: 2em");

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
        shuffleButton.setStyle("-fx-font-size: 2em;");

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

