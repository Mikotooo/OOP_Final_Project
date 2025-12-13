package tetris.core;

public class ScoreManager {
    private int score = 0;
    private int lines = 0;
    private int level = 1;

    public void reset() {
        score = 0;
        lines = 0;
        level = 1;
    }

    public void onLinesCleared(int cleared) {
        if (cleared <= 0) return;

        // classic-ish scoring (line clears)
        // 1: 40, 2:100, 3:300, 4:1200 multiplied by level
        int add;
        switch (cleared) {
            case 1 -> add = 40;
            case 2 -> add = 100;
            case 3 -> add = 300;
            default -> add = 1200; // 4
        }
        score += add * level;

        lines += cleared;
        level = 1 + (lines / 10);
    }

    public void onSoftDropStep() {
        // small reward for soft drop
        score += 1;
    }

    public void onHardDrop(int distance) {
        // reward for hard drop distance
        score += distance * 2;
    }

    // gravity speed
    public int getDelayMs() {
        // faster with level; clamp so it doesn't go insane
        int base = 550;
        int delay = base - (level - 1) * 45;
        return Math.max(90, delay);
    }

    public int getScore() { return score; }
    public int getLines() { return lines; }
    public int getLevel() { return level; }
}
