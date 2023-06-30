package com.example.taskfordevcom.domain.piece;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PieceTab {
    private TabEllipse ellipse;
    private TabRectangle rectangle;
    private TabCircle circle1, circle2;


}
