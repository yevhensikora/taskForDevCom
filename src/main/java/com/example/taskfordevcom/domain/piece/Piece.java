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


@Builder
public class Piece extends Parent {

    public static final int SIZE = 100;
    private Image image;
    private final double correctX;
    private final double correctY;
    private final boolean hasTopTab;
    private final boolean hasLeftTab;
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


    public Piece(Image image, double correctX, double correctY, boolean topTab, boolean leftTab, boolean bottomTab, boolean rightTab,
                 double deskWidth, double deskHeight,double startDragX,double startDragY,Point2D dragAnchor,double ellipseRadiusX,double ellipseRadiusY,
                 Piece leftNeighbor, Piece topNeighbor, double neighborX, double neighborY){
        this.image = image;
        this.correctX = correctX;
        this.correctY = correctY;
        this.hasTopTab = topTab;
        this.hasLeftTab = leftTab;
        this.hasBottomTab = bottomTab;
        this.hasRightTab = rightTab;
        this.deskWidth = deskWidth;
        this.deskHeight = deskHeight;
        this.startDragX = startDragX;
        this.startDragY = startDragY;
        this.dragAnchor = dragAnchor;
        this.ellipseRadiusX = topTab || bottomTab ? correctX : 0;
        this.ellipseRadiusY = leftTab || rightTab ? correctY : 0;
        this.leftNeighbor = leftNeighbor;
        this.topNeighbor = topNeighbor;

        calculateTabSize();
        initPiece();
    }

    private void initPiece() {
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
            shape = Shape.union(shape, createBottomTab());
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
        return createPieceTab(PieceTab.builder()
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
                .build());
    }

    private Shape createBottomTab() {
        return createPieceTab(PieceTab.builder()
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
                .build());
    }

    private Shape createLeftTab() {
        return createPieceTab(PieceTab.builder()
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
                .build());
    }

    private Shape createTopTab() {
        return createPieceTab(PieceTab.builder()
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
                .build());
    }


    private Rectangle createPieceRectangle() {
        Rectangle rec = new Rectangle();
        rec.setX(-50);
        rec.setY(-50);
        rec.setWidth(SIZE);
        rec.setHeight(SIZE);
        return rec;
    }

    private Shape createPieceTab(PieceTab pieceTab) {
        TabEllipse tabEllipse = pieceTab.getEllipse();
        TabRectangle tabRectangle = pieceTab.getRectangle();
        TabCircle tabCircle1 = pieceTab.getCircle1();
        TabCircle tabCircle2 = pieceTab.getCircle2();

        Ellipse ellipse = new Ellipse(tabEllipse.getEllipseCenterX(), tabEllipse.getEllipseCenterY(),
                tabEllipse.getEllipseRadiusX(), tabEllipse.getEllipseRadiusY() );
        Rectangle rectangle = new Rectangle(tabRectangle.getRectangleX(), tabRectangle.getRectangleY(),
                tabRectangle.getRectangleWidth(), tabRectangle.getRectangleHeight());
        Circle circle1 = new Circle(tabCircle1.getCircleCenterX(), tabCircle1.getCircleCenterY(), tabCircle1.getCircleRadius());
        Circle circle2 = new Circle(tabCircle2.getCircleCenterX(), tabCircle2.getCircleCenterY(), tabCircle2.getCircleRadius());

        Shape tab = Shape.union(ellipse, rectangle);
        tab = Shape.subtract(tab, circle1);
        tab = Shape.subtract(tab, circle2);
        return tab;
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

    public double getCorrectX() {
        return correctX;
    }

    public double getCorrectY() {
        return correctY;
    }
}

