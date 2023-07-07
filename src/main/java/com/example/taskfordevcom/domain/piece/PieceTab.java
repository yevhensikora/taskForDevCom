package com.example.taskfordevcom.domain.piece;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class PieceTab {
    private TabEllipse ellipse;
    private TabRectangle rectangle;
    private TabCircle circle1;
    private TabCircle circle2;

    public PieceTab(PieceTab pieceTab) {
        this.ellipse = pieceTab.getEllipse();
        this.rectangle = pieceTab.getRectangle();
        this.circle1 = pieceTab.getCircle1();
        this.circle2 = pieceTab.getCircle2();
    }

}
