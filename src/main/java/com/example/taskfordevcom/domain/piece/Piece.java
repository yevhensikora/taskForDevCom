package com.example.taskfordevcom.domain.piece;


import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javafx.geometry.Point2D;
import lombok.*;

import java.util.Random;

import static com.example.taskfordevcom.domain.piece.TabType.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Piece extends Parent {

    public static final int SIZE = 100;
    private Image image;
    private final double correctX;
    private final double correctY;
    public final boolean hasTopTab;
    public final boolean hasLeftTab;
    private final boolean hasBottomTab;
    private final boolean hasRightTab;
    private final double deskWidth;
    private final double deskHeight;
    private double startDragX;
    private double startDragY;
    private Point2D dragAnchor;


    private double ellipseRadiusX;
    private double ellipseRadiusY;
    private Piece leftNeighbor;
    private Piece topNeighbor;
    private double neighborX;
    private double neighborY;

    private PieceTab topTab;
    private PieceTab rightTab;
    private PieceTab leftTab;
    private PieceTab bottomTab;

    private Piece rightNeighbor;
    private int col;
    private int row;


    public void initPiece() {
        this.ellipseRadiusX = hasTopTab || hasBottomTab ? correctX : 0;
        this.ellipseRadiusY = hasLeftTab || hasRightTab ? correctY : 0;
        calculateTabSize();

        Shape pieceClip = createPiece();
        pieceClip.setFill(Color.WHITE);
        pieceClip.setStroke(null);

        Shape pieceStroke = createPiece();
        pieceStroke.setFill(null);
        pieceStroke.setStroke(Color.BLACK);

        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setClip(pieceClip);
        setFocusTraversable(true);
        getChildren().addAll(imageView, pieceStroke);

        setCache(true);
        setInactive();

        setOnMousePressed(me -> {
            toFront();
            startDragX = getTranslateX();
            startDragY = getTranslateY();
            dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
        });

        setOnMouseReleased(me -> {
            if (getTranslateX() < 10 && getTranslateX() > -10 &&
                    getTranslateY() < 10 && getTranslateY() > -10) {
                setTranslateX(0);
                setTranslateY(0);
                setInactive();
            }
        });

        setOnMouseDragged(me -> {
            double newTranslateX = startDragX + me.getSceneX() - dragAnchor.getX();
            double newTranslateY = startDragY + me.getSceneY() - dragAnchor.getY();
            double minTranslateX = -45 - correctX;
            double maxTranslateX = (deskWidth - SIZE + 50) - correctX;
            double minTranslateY = -30 - correctY;
            double maxTranslateY = (deskHeight - SIZE + 70) - correctY;
            if (newTranslateX > minTranslateX &&
                    newTranslateX < maxTranslateX &&
                    newTranslateY > minTranslateY &&
                    newTranslateY < maxTranslateY) {
                setTranslateX(newTranslateX);
                setTranslateY(newTranslateY);
            }
        });
    }

    private Shape createPiece() {
        Shape shape = createPieceRectangle();
        if (hasRightTab) {
            shape = Shape.union(shape, createRightTab());
        }
        if (hasBottomTab) {
            shape = Shape.union(shape, createBottomTab() );
        }
        if (hasLeftTab) {
            shape = Shape.subtract(shape, createLeftTab());
        }
        if (hasTopTab) {
            shape = Shape.subtract(shape, createTopTab());
        }

        shape.setTranslateX(correctX);
        shape.setTranslateY(correctY);
        shape.setLayoutX(50);
        shape.setLayoutY(50);
        return shape;
    }

    private Shape createRightTab() {
        PieceTab pieceTab = PieceTab.builder()
                .ellipse(TabEllipse.builder()
                        .ellipseCenterX(69.5)
                        .ellipseCenterY(0)
                        .ellipseRadiusX(10)
                        .ellipseRadiusY(ellipseRadiusY)
                        .build())
                .rectangle(TabRectangle.builder()
                        .rectangleX(50)
                        .rectangleY(-12.5)
                        .rectangleWidth(11.5)
                        .rectangleHeight(25)
                        .build())
                .circle1(TabCircle.builder()
                        .circleCenterX(56.25)
                        .circleCenterY(-14)
                        .circleRadius(6.25)
                        .build())
                .circle2(TabCircle.builder()
                        .circleCenterX(56.25)
                        .circleCenterY(14)
                        .circleRadius(6.25)
                        .build())
                .build();
        return createPieceTab(pieceTab, RIGHT);
    }

    private Shape createBottomTab() {
        PieceTab pieceTab = PieceTab.builder()
                .ellipse(TabEllipse.builder()
                        .ellipseCenterX(0)
                        .ellipseCenterY(69.5)
                        .ellipseRadiusX(ellipseRadiusX)
                        .ellipseRadiusY(10)
                        .build())
                .rectangle(TabRectangle.builder()
                        .rectangleX(-12.5)
                        .rectangleY(50)
                        .rectangleWidth(25)
                        .rectangleHeight(11)
                        .build())
                .circle1(TabCircle.builder()
                        .circleCenterX(-14)
                        .circleCenterY(56.25)
                        .circleRadius(6.25)
                        .build())
                .circle2(TabCircle.builder()
                        .circleCenterX(14)
                        .circleCenterY(56.25)
                        .circleRadius(6.25)
                        .build())
                .build();
        return createPieceTab(pieceTab, BOTTOM);
    }

    private Shape createLeftTab() {
        PieceTab pieceTab = PieceTab.builder()
                .ellipse(TabEllipse.builder()
                        .ellipseCenterX(-31)
                        .ellipseCenterY(0)
                        .ellipseRadiusX(10)
                        .ellipseRadiusY(neighborY)
                        .build())
                .rectangle(TabRectangle.builder()
                        .rectangleX(-50)
                        .rectangleY(-12.5)
                        .rectangleWidth(11)
                        .rectangleHeight(25)
                        .build())
                .circle1(TabCircle.builder()
                        .circleCenterX(-43.75)
                        .circleCenterY(-14)
                        .circleRadius(6.25)
                        .build())
                .circle2(TabCircle.builder()
                        .circleCenterX(-43.75)
                        .circleCenterY(14)
                        .circleRadius(6.25)
                        .build())
                .build();
        return createPieceTab(pieceTab, LEFT);
    }

    private Shape createTopTab() {
        PieceTab pieceTab = PieceTab.builder()
                .ellipse(TabEllipse.builder()
                        .ellipseCenterX(0)
                        .ellipseCenterY(-31)
                        .ellipseRadiusX(neighborX)
                        .ellipseRadiusY(10)
                        .build())
                .rectangle(TabRectangle.builder()
                        .rectangleX(-12.5)
                        .rectangleY(-50)
                        .rectangleWidth(25)
                        .rectangleHeight(12.5)
                        .build())
                .circle1(TabCircle.builder()
                        .circleCenterX(-14)
                        .circleCenterY(-43.75)
                        .circleRadius(6.25)
                        .build())
                .circle2(TabCircle.builder()
                        .circleCenterX(14)
                        .circleCenterY(-43.75)
                        .circleRadius(6.25)
                        .build())
                .build();
        return createPieceTab(pieceTab, TOP);
    }


    private Rectangle createPieceRectangle() {
        Rectangle rec = new Rectangle();
        rec.setX(-50);
        rec.setY(-50);
        rec.setWidth(SIZE);
        rec.setHeight(SIZE);
        return rec;
    }

    private Shape createPieceTab(PieceTab pieceTab, TabType tabType) {
        TabEllipse tabEllipse = pieceTab.getEllipse();
        TabRectangle tabRectangle = pieceTab.getRectangle();
        TabCircle tabCircle1 = pieceTab.getCircle1();
        TabCircle tabCircle2 = pieceTab.getCircle2();

        Ellipse ellipse = new Ellipse(tabEllipse.getEllipseCenterX(), tabEllipse.getEllipseCenterY(),
                tabEllipse.getEllipseRadiusX(), tabEllipse.getEllipseRadiusY());
        Rectangle rectangle = new Rectangle(tabRectangle.getRectangleX(), tabRectangle.getRectangleY(),
                tabRectangle.getRectangleWidth(), tabRectangle.getRectangleHeight());
        Circle circle1 = new Circle(tabCircle1.getCircleCenterX(), tabCircle1.getCircleCenterY(), tabCircle1.getCircleRadius());
        Circle circle2 = new Circle(tabCircle2.getCircleCenterX(), tabCircle2.getCircleCenterY(), tabCircle2.getCircleRadius());

        setTabParams(pieceTab, tabType);

        Shape tab = Shape.union(ellipse, rectangle);
            tab = Shape.subtract(tab, circle1);
            tab = Shape.subtract(tab, circle2);
            return tab;

    }

    private void setTabParams(PieceTab pieceTab,TabType tabType) {
        switch (tabType) {
            case RIGHT -> this.rightTab = new PieceTab(pieceTab);
            case LEFT -> this.leftTab = new PieceTab(pieceTab);
            case TOP -> this.topTab = new PieceTab(pieceTab);
            case BOTTOM -> this.bottomTab = new PieceTab(pieceTab);
        }
    }

    private void calculateTabSize() {
        double lowerLimitX = 7;
        double upperLimitX = 23;

        double lowerLimitY = 7;
        double upperLimitY = 23;


        Random random = new Random();
        double randomValueX = lowerLimitX + (upperLimitX - lowerLimitX) * random.nextDouble();
        double randomValueY = lowerLimitY + (upperLimitY - lowerLimitY) * random.nextDouble();

        if (hasRightTab) {
            ellipseRadiusY = randomValueY;
        }

        if (hasBottomTab) {
            ellipseRadiusX = randomValueX;
        }

        if (hasLeftTab) {
            if (leftNeighbor != null) {
                neighborY = leftNeighbor.ellipseRadiusY;
            }
        }

        if (hasTopTab) {
            if (topNeighbor != null) {
                neighborX = topNeighbor.ellipseRadiusX;
            }
        }
    }

    public void setActive() {
        setDisable(false);
        setEffect(new DropShadow());
        toFront();
    }

    public void setInactive() {
        setEffect(null);
        setDisable(true);
        toBack();
    }
}