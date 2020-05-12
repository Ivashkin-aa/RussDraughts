package controller;

import model.Figure;

public class Controller {
    private Figure figure;
    private Step type;

    public Figure getFigure() {
        return figure;
    }

    public Step getType() {
        return type;
    }

    public Controller(Step type) {
        this(null, type);
    }

    public Controller(Figure figure, Step type) {
        this.figure = figure;
        this.type = type;
    }


}
