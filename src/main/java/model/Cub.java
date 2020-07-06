package model;

import view.FigureView;

public class Cub {
    public FigureView figure;

    public FigureView getFigure() {
        return figure;
    }

    public boolean hasFigure() {
        return figure != null;
    }

    public void setFigure(FigureView figure) {
        this.figure = figure;
    }
}
