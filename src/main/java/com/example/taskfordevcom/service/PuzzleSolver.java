package com.example.taskfordevcom.service;

import com.example.taskfordevcom.domain.piece.Piece;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.List;

public class PuzzleSolver {
    private final Piece[][] puzzlePieces;
    private Timeline timeline;

    public PuzzleSolver(List<Piece> pieces, int numOfRows, int numOfColumns) {
        this.puzzlePieces = new Piece[numOfRows][numOfColumns];
        for (int row = 0, i = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfColumns && i < pieces.size(); col++, i++) {
                puzzlePieces[row][col] = pieces.get(i);
            }
        }
    }


    public void solvePuzzle(int rows, int columns) {
        timeline = new Timeline();

        // Додавання KeyFrames для кожного пазлу
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Piece piece = puzzlePieces[row][col];
                if (piece != null) {
                    // Встановлення початкових значень переміщення
                    double startX = piece.getTranslateX();
                    double startY = piece.getTranslateY();
                    double endX = (col * Piece.SIZE) - piece.getCorrectX();
                    double endY = (row * Piece.SIZE) - piece.getCorrectY();

                    // Додавання KeyFrame з анімацією переміщення
                    timeline.getKeyFrames().add(
                            new KeyFrame(Duration.seconds(1),
                                    new KeyValue(piece.translateXProperty(), endX),
                                    new KeyValue(piece.translateYProperty(), endY)
                            )
                    );

                    // Встановлення початкових значень переміщення після анімації
                    piece.setTranslateX(startX);
                    piece.setTranslateY(startY);
                }
            }
        }

        // Запуск анімації
        timeline.play();
        Piece topLeftPiece = findTopLeftPiece();
        if (topLeftPiece != null) {
            connectPieces(topLeftPiece, 0, 0);
            topLeftPiece.setInactive();
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Piece matchingPiece = findMatchingPiece(row, col);

                if (matchingPiece != null) {
                    connectPieces(matchingPiece, col, row);
                    matchingPiece.setInactive();
                }
            }
        }
    }

    private Piece findMatchingPiece(int row, int col) {
        Piece leftPiece = getLeftPiece(row, col);
        Piece topPiece = getTopPiece(row, col);

        for (int i = 0; i < puzzlePieces.length; i++) {
            for (int j = 0; j < puzzlePieces[i].length; j++) {
                Piece currentPiece = puzzlePieces[i][j];
                if (currentPiece != null && isMatchingPiece(currentPiece, leftPiece, topPiece)) {
                    puzzlePieces[i][j] = null;
                    return currentPiece;
                }
            }
        }
        return null;
    }

    private boolean isMatchingPiece(Piece currentPiece, Piece leftPiece, Piece topPiece) {
        if (topPiece != null && leftPiece != null && currentPiece.getLeftTab() != null && currentPiece.getTopTab() != null && currentPiece.getTopTab().getEllipse() != null
                && currentPiece.getLeftTab().getEllipse() != null && leftPiece.getRightTab() != null && leftPiece.getRightTab().getEllipse() != null
                && topPiece.getBottomTab() != null && topPiece.getBottomTab().getEllipse() != null) {
            return (leftPiece.getRightTab().getEllipse().getEllipseRadiusY() == currentPiece.getLeftTab().getEllipse().getEllipseRadiusY()
                    && topPiece.getBottomTab().getEllipse().getEllipseRadiusX() == currentPiece.getTopTab().getEllipse().getEllipseRadiusX());
        } else if (topPiece != null && currentPiece.getTopTab() != null && currentPiece.getTopTab().getEllipse() != null &&
                topPiece.getBottomTab() != null && topPiece.getBottomTab().getEllipse() != null) {
            return (topPiece.getBottomTab().getEllipse().getEllipseRadiusX() == currentPiece.getTopTab().getEllipse().getEllipseRadiusX());
        } else if (leftPiece != null && currentPiece.getLeftTab() != null && currentPiece.getLeftTab().getEllipse() != null &&
                leftPiece.getRightTab() != null && leftPiece.getRightTab().getEllipse() != null) {
            return (leftPiece.getRightTab().getEllipse().getEllipseRadiusY() == currentPiece.getLeftTab().getEllipse().getEllipseRadiusY());
        }
            return leftPiece == null && topPiece == null;
    }

    private Piece getLeftPiece(int row, int col) {
        if (col > 0) {
            return puzzlePieces[row][col - 1];
        }
        return null;
    }

    private Piece getTopPiece(int row, int col) {
        if (row > 0) {
            return puzzlePieces[row - 1][col];
        }
        return null;
    }

    private Piece findTopLeftPiece() {
        for (Piece[] puzzlePiece : puzzlePieces) {
            for (Piece piece : puzzlePiece) {
                if (piece != null && !piece.hasLeftTab && !piece.hasTopTab) {
                    return piece;
                }
            }
        }
        return null;
    }

    private void connectPieces(Piece piece, double x, double y) {
        double pieceSize = Piece.SIZE;
        double offsetX = x * pieceSize;
        double offsetY = y * pieceSize;
        double translationX = offsetX - piece.getCorrectX();
        double translationY = offsetY - piece.getCorrectY();
        piece.setTranslateX(translationX);
        piece.setTranslateY(translationY);
    }
}