package com.example.taskfordevcom.domain.piece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TabCircle {
    double circleCenterX;
    double circleCenterY;
    double circleRadius;
}
