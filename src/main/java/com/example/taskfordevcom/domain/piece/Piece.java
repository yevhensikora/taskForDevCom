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

    public Piece(Image image, double correctX, double correctY,
                 boolean topTab, boolean leftTab, boolean bottomTab, boolean rightTab,
                 double deskWidth, double deskHeight,double startDragX,double startDragY,Point2D dragAnchor){
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
            shape = Shape.union(shape,
                    createPieceTab(PieceTab.builder()
                            .ellipse(TabEllipse.builder()
                                    .ellipseCenterX(69.5f)
                                    .ellipseCenterY(0f)
                                    .ellipseRadiusX(10f)
                                    .ellipseRadiusY(17.5f)
                                    .build())
                            .rectangle(TabRectangle.builder()
                                    .rectangleX(50f)
                                    .rectangleY(-12.5f)
                                    .rectangleWidth(11.5f)
                                    .rectangleHeight(25f)
                                    .build())
                            .circle1(TabCircle.builder()
                                    .circleCenterX(56.25f)
                                    .circleCenterY(-14f)
                                    .circleRadius(6.25f)
                                    .build())
                            .circle2(TabCircle.builder()
                                    .circleCenterX(56.25f)
                                    .circleCenterY(14f)
                                    .circleRadius(6.25f)
                                    .build())
                            .build()));

        }
        if (hasBottomTab) {
            shape = Shape.union(shape,
                    createPieceTab(PieceTab.builder()
                            .ellipse(TabEllipse.builder()
                                    .ellipseCenterX(0f)
                                    .ellipseCenterY(69.5f)
                                    .ellipseRadiusX(17.5f)
                                    .ellipseRadiusY(10f)
                                    .build())
                            .rectangle(TabRectangle.builder()
                                    .rectangleX(-12.5f)
                                    .rectangleY(50f)
                                    .rectangleWidth(25f)
                                    .rectangleHeight(11f)
                                    .build())
                            .circle1(TabCircle.builder()
                                    .circleCenterX(-14f)
                                    .circleCenterY(56.25f)
                                    .circleRadius(6.25f)
                                    .build())
                            .circle2(TabCircle.builder()
                                    .circleCenterX(14f)
                                    .circleCenterY(56.25f)
                                    .circleRadius(6.25f)
                                    .build())
                            .build()));
        }
        if (hasLeftTab) {
            shape = Shape.subtract(shape,
                    createPieceTab(PieceTab.builder()
                            .ellipse(TabEllipse.builder()
                                    .ellipseCenterX(-31)
                                    .ellipseCenterY(0)
                                    .ellipseRadiusX(10)
                                    .ellipseRadiusY(17.5)
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
                            .build()));
        }
        if (hasTopTab) {
            shape = Shape.subtract(shape,
                    createPieceTab(PieceTab.builder()
                            .ellipse(TabEllipse.builder()
                                    .ellipseCenterX(0)
                                    .ellipseCenterY(-31)
                                    .ellipseRadiusX(17.5)
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
                            .build()));
        }

        shape.setTranslateX(correctX);
        shape.setTranslateY(correctY);
        shape.setLayoutX(50f);
        shape.setLayoutY(50f);
        return shape;
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
                tabEllipse.getEllipseRadiusX(), tabEllipse.getEllipseRadiusY());
        Rectangle rectangle = new Rectangle(tabRectangle.getRectangleX(), tabRectangle.getRectangleY(),
                tabRectangle.getRectangleWidth(), tabRectangle.getRectangleHeight());
        Circle circle1 = new Circle(tabCircle1.getCircleCenterX(), tabCircle1.getCircleCenterY(), tabCircle1.getCircleRadius());
        Circle circle2 = new Circle(tabCircle2.getCircleCenterX(), tabCircle2.getCircleCenterY(), tabCircle2.getCircleRadius());

        Shape tab = Shape.union(ellipse, rectangle);
        tab = Shape.subtract(tab, circle1);
        tab = Shape.subtract(tab, circle2);
        return tab;
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

