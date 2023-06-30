package com.example.taskfordevcom.domain.piece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TabRectangle {
    double rectangleX;
    double rectangleY;
    double rectangleWidth;
    double rectangleHeight;
}
