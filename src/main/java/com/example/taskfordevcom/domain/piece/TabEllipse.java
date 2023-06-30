package com.example.taskfordevcom.domain.piece;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TabEllipse {
    double ellipseCenterX;
    double ellipseCenterY;
    double ellipseRadiusX;
    double ellipseRadiusY;
}
