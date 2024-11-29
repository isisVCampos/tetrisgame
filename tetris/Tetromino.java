import java.awt.*;
import java.util.Random;

public class Tetromino {
    public Point[] shape;
    public Color color;
    public int x, y;

    public Tetromino(Point[] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 4; // Posição inicial horizontal
        this.y = 0; // Posição inicial vertical
    }

    public static Tetromino getRandomTetromino() {
        Random rand = new Random();
        TetrominoType type = TetrominoType.values()[rand.nextInt(TetrominoType.values().length)];

        switch (type) {
            case I: return new Tetromino(new Point[] { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0) }, Color.CYAN);
            case O: return new Tetromino(new Point[] { new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1) }, Color.YELLOW);
            case T: return new Tetromino(new Point[] { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(1, 1) }, Color.MAGENTA);
            case S: return new Tetromino(new Point[] { new Point(0, 1), new Point(1, 1), new Point(1, 0), new Point(2, 0) }, Color.GREEN);
            case Z: return new Tetromino(new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }, Color.RED);
            case J: return new Tetromino(new Point[] { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1) }, Color.BLUE);
            case L: return new Tetromino(new Point[] { new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(0, 1) }, Color.ORANGE);
            default: return null;
        }
    }
    public enum TetrominoType {
        I, O, T, S, Z, J, L
    }

    public Tetromino rotate() {
        Point[] newShape = new Point[shape.length];
        for (int i = 0; i < shape.length; i++) {
            Point p = shape[i];
            newShape[i] = new Point(-p.y, p.x);
        }

        return new Tetromino(newShape, color);
    }
}
