package tetris.core;

import java.awt.Point;

public class Tetromino {

    @FunctionalInterface
    public interface BlockConsumer {
        void accept(int x, int y, int colorIndex);
    }

    @FunctionalInterface
    public interface PreviewConsumer {
        void accept(int px, int py, int colorIndex);
    }

    private final ShapeType type;
    private int rotation; 
    private int x;        // board position (top-left of 4x4)
    private int y;

    // Each shape: 4 rotations; each rotation: 4 blocks
    private static final Point[][][] SHAPES = new Point[ShapeType.values().length][4][4];

    static {
        // Coordinates inside a 4x4 box
        // I
        SHAPES[ShapeType.I.ordinal()] = new Point[][] {
                { p(0,1), p(1,1), p(2,1), p(3,1) },
                { p(2,0), p(2,1), p(2,2), p(2,3) },
                { p(0,2), p(1,2), p(2,2), p(3,2) },
                { p(1,0), p(1,1), p(1,2), p(1,3) }
        };
        // O
        SHAPES[ShapeType.O.ordinal()] = new Point[][] {
                { p(1,1), p(2,1), p(1,2), p(2,2) },
                { p(1,1), p(2,1), p(1,2), p(2,2) },
                { p(1,1), p(2,1), p(1,2), p(2,2) },
                { p(1,1), p(2,1), p(1,2), p(2,2) }
        };
        // T
        SHAPES[ShapeType.T.ordinal()] = new Point[][] {
                { p(1,1), p(0,2), p(1,2), p(2,2) },
                { p(1,1), p(1,2), p(2,2), p(1,3) },
                { p(0,2), p(1,2), p(2,2), p(1,3) },
                { p(1,1), p(0,2), p(1,2), p(1,3) }
        };
        // S
        SHAPES[ShapeType.S.ordinal()] = new Point[][] {
                { p(1,1), p(2,1), p(0,2), p(1,2) },
                { p(1,1), p(1,2), p(2,2), p(2,3) },
                { p(1,2), p(2,2), p(0,3), p(1,3) },
                { p(0,1), p(0,2), p(1,2), p(1,3) }
        };
        // Z
        SHAPES[ShapeType.Z.ordinal()] = new Point[][] {
                { p(0,1), p(1,1), p(1,2), p(2,2) },
                { p(2,1), p(1,2), p(2,2), p(1,3) },
                { p(0,2), p(1,2), p(1,3), p(2,3) },
                { p(1,1), p(0,2), p(1,2), p(0,3) }
        };
        // J
        SHAPES[ShapeType.J.ordinal()] = new Point[][] {
                { p(0,1), p(0,2), p(1,2), p(2,2) },
                { p(1,1), p(2,1), p(1,2), p(1,3) },
                { p(0,2), p(1,2), p(2,2), p(2,3) },
                { p(1,1), p(1,2), p(0,3), p(1,3) }
        };
        // L
        SHAPES[ShapeType.L.ordinal()] = new Point[][] {
                { p(2,1), p(0,2), p(1,2), p(2,2) },
                { p(1,1), p(1,2), p(1,3), p(2,3) },
                { p(0,2), p(1,2), p(2,2), p(0,3) },
                { p(0,1), p(1,1), p(1,2), p(1,3) }
        };
    }

    private static Point p(int x, int y) { return new Point(x, y); }

    public Tetromino(ShapeType type) {
        this.type = type;
        this.rotation = 0;
        this.x = 3;   // good spawn for 10-wide board
        this.y = -2;  // spawn slightly above visible top
    }

    public ShapeType getType() { return type; }
    public int getRotation() { return rotation; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int colorIndex() { return type.colorIndex(); }

    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public void move(int dx, int dy) { this.x += dx; this.y += dy; }

    public void rotateCW() { rotation = (rotation + 1) & 3; }
    public void rotateCCW() { rotation = (rotation + 3) & 3; }

    public void forEachBlock(BlockConsumer c) {
        Point[] blocks = SHAPES[type.ordinal()][rotation];
        int col = type.colorIndex();
        for (Point b : blocks) {
            int bx = x + b.x;
            int by = y + b.y;
            c.accept(bx, by, col);
        }
    }

    // For "Next" preview rendering (normalized in a small box)
    public void forEachPreviewBlock(PreviewConsumer c) {
        Point[] blocks = SHAPES[type.ordinal()][0];
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        for (Point b : blocks) { minX = Math.min(minX, b.x); minY = Math.min(minY, b.y); }

        int col = type.colorIndex();
        for (Point b : blocks) {
            c.accept(b.x - minX, b.y - minY, col);
        }
    }

    public Tetromino copy() {
        Tetromino t = new Tetromino(this.type);
        t.rotation = this.rotation;
        t.x = this.x;
        t.y = this.y;
        return t;
    }

    public void setRotation(int r) { rotation = r & 3; }
}