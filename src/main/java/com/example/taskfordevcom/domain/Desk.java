package com.example.taskfordevcom.domain;

import com.example.taskfordevcom.domain.piece.Piece;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Desk extends Pane {

    private static final String STYLE = """
            -fx-background-color: #cccccc;
            -fx-border-color: #464646;
            -fx-effect: innershadow(two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 )
            """;
    private int numColumns;
    private int numRows;

    public Desk(int numOfRows, int numOfColumns) {
        this.numRows = numOfRows;
        this.numColumns = numOfColumns;

        setStyle(STYLE);
        setPrefSize(Piece.SIZE * numColumns, Piece.SIZE * numRows);

        double DESK_WIDTH = Piece.SIZE * numColumns;
        double DESK_HEIGHT = Piece.SIZE * numRows;

        setPrefSize(DESK_WIDTH, DESK_HEIGHT);
        setMaxSize(DESK_WIDTH, DESK_HEIGHT);
        autosize();
        drawGrid();
    }

    public void drawGrid() {

        getChildren().removeIf(node -> node instanceof Path);

        Path grid = new Path();
        grid.setStroke(Color.rgb(70, 70, 70));
        getChildren().add(grid);

        for (int col = 0; col < numColumns - 1; col++) {
            grid.getElements().addAll(
                    new MoveTo(Piece.SIZE + Piece.SIZE * col, 5),
                    new LineTo(Piece.SIZE + Piece.SIZE * col, Piece.SIZE * numRows - 5)
            );
        }

        for (int row = 0; row < numRows - 1; row++) {
            grid.getElements().addAll(
                    new MoveTo(5, Piece.SIZE + Piece.SIZE * row),
                    new LineTo(Piece.SIZE * numColumns - 5, Piece.SIZE + Piece.SIZE * row)
            );
        }
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        setPrefSize(Piece.SIZE * numColumns, Piece.SIZE * numRows);
        drawGrid();
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        setPrefSize(Piece.SIZE * numColumns, Piece.SIZE * numRows);
        drawGrid();
    }
}

