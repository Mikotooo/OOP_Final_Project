package tetris.core;

public enum ShapeType {
    I(1), O(2), T(3), S(4), Z(5), J(6), L(7);

    private final int colorIndex;

    ShapeType(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public int colorIndex() {
        return colorIndex;
    }
}
