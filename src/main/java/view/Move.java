package view;

public class Move {
    private final FigureView figure;
    private final Step type;

    public FigureView getFigure() {
        return figure;
    }

    public Step getType() {
        return type;
    }

    public Move(Step type) {
        this(null, type);
    }

    public Move(FigureView figure, Step type) {
        this.figure = figure;
        this.type = type;
    }

}
