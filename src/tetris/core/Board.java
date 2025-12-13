package tetris.core;

public class Board {
    private final int width;
    private final int height;
    private final int[][] grid; 

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new int[height][width];
    }

    public int[][] getGrid() {
        return grid;
    }

    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) grid[y][x] = 0;
        }
    }

    public boolean canPlace(Tetromino t) {
        final boolean[] ok = { true };
        t.forEachBlock((x, y, colorIdx) -> {
            if (!ok[0]) return;

            // allow blocks above the board (spawn area)
            if (y < 0) return;

            if (x < 0 || x >= width || y >= height) {
                ok[0] = false;
                return;
            }
            if (grid[y][x] != 0) ok[0] = false;
        });
        return ok[0];
    }

    public void lock(Tetromino t) {
        t.forEachBlock((x, y, colorIdx) -> {
            if (y < 0) return; 
            if (y >= 0 && y < height && x >= 0 && x < width) {
                grid[y][x] = colorIdx;
            }
        });
    }

    public int clearLines() {
        int cleared = 0;

        for (int y = height - 1; y >= 0; y--) {
            boolean full = true;
            for (int x = 0; x < width; x++) {
                if (grid[y][x] == 0) { full = false; break; }
            }

            if (full) {
                cleared++;

                // shift down
                for (int row = y; row > 0; row--) {
                    System.arraycopy(grid[row - 1], 0, grid[row], 0, width);
                }
                // clear top row
                for (int x = 0; x < width; x++) grid[0][x] = 0;

                // re-check same y because we pulled rows down
                y++;
            }
        }

        return cleared;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
