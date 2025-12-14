package tetris.ui;

import tetris.core.Game;
import tetris.core.ScoreManager;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements KeyListener {

    // Board dimensions
    private static final int BOARD_W = 10;
    private static final int BOARD_H = 20;

    // Game Boy-ish pixels (chunky)
    private static final int CELL = 18;

    private static final int SIDE_W = 140;
    private static final int PAD = 16;
    private final Game game;
    private Timer timer; 

    // Game Boy style colors
    private static final Color FRAME_DARK = new Color(30, 36, 24);
    private static final Color FRAME_LIGHT = new Color(63, 74, 45);

    private static final Color LCD_LIGHT = new Color(198, 216, 167);
    private static final Color LCD_MID   = new Color(142, 172, 105);
    private static final Color LCD_DARK  = new Color(54, 76, 41);
    private static final Color LCD_DEEP  = new Color(28, 42, 22);

    // “Blocks” are just different green shades (classic Game Boy vibe)
    // index 0 empty; 1..7 pieces
    private final Color[] colors = new Color[] {
            LCD_LIGHT,  // empty cell = light screen
            LCD_DARK,   // I
            LCD_DARK,   // O
            LCD_DARK,   // T
            LCD_DARK,   // S
            LCD_DARK,   // Z
            LCD_DARK,   // J
            LCD_DARK    // L
    };

    private final Font titleFont = new Font("Monospaced", Font.BOLD, 13);
    private final Font valueFont = new Font("Monospaced", Font.PLAIN, 13);
    private final Font stateFont = new Font("Monospaced", Font.BOLD, 16);

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);

        this.game = new Game(BOARD_W, BOARD_H);

        int screenW = BOARD_W * CELL;
        int screenH = BOARD_H * CELL;

        int w = PAD + screenW + PAD + SIDE_W + PAD;
        int h = PAD + screenH + PAD;

        setPreferredSize(new Dimension(w, h));

        this.timer = new Timer(game.getScore().getDelayMs(), e -> {
            game.tick();
            this.timer.setDelay(game.getScore().getDelayMs());
            repaint();
        });
        this.timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int screenX = PAD;
        int screenY = PAD;
        int screenW = BOARD_W * CELL;
        int screenH = BOARD_H * CELL;

        drawFrame(g);

        g.setColor(LCD_LIGHT);
        g.fillRect(screenX, screenY, screenW, screenH);

        g.setColor(new Color(0, 0, 0, 18));
        for (int y = screenY; y < screenY + screenH; y += 4) {
            g.drawLine(screenX, y, screenX + screenW - 1, y);
        }

        int[][] grid = game.getBoard().getGrid();
        for (int y = 0; y < BOARD_H; y++) {
            for (int x = 0; x < BOARD_W; x++) {
                drawCell(g, screenX, screenY, x, y, grid[y][x]);
            }
        }

        game.getActive().forEachBlock((bx, by, colorIdx) -> {
            if (by >= 0) drawCell(g, screenX, screenY, bx, by, colorIdx);
        });

        g.setColor(LCD_DEEP);
        g.drawRect(screenX - 1, screenY - 1, screenW + 1, screenH + 1);
        g.setColor(LCD_MID);
        g.drawRect(screenX - 2, screenY - 2, screenW + 3, screenH + 3);

        int hudX = screenX + screenW + PAD;
        int hudY = PAD;
        int hudW = SIDE_W;
        int hudH = screenH;

        drawHud(g, hudX, hudY, hudW, hudH);

        String state = game.getStateName();
        if (!"RUNNING".equals(state)) {
            drawStateOverlay(g, screenX, screenY, screenW, screenH, state);
        }
    }

    private void drawFrame(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(FRAME_DARK);
        g.fillRect(0, 0, w, h);

        g.setColor(FRAME_LIGHT);
        g.drawRect(6, 6, w - 13, h - 13);

        g.setColor(new Color(0,0,0,80));
        g.drawRect(10, 10, w - 21, h - 21);

        // little “Game Boy” label vibe (simple)
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        g.setColor(new Color(220, 220, 220, 90));
        g.drawString("TETRIS", PAD, 14);
    }

    private void drawCell(Graphics g, int ox, int oy, int x, int y, int colorIdx) {
        int px = ox + x * CELL;
        int py = oy + y * CELL;

        if (colorIdx == 0) {
            // empty cell
            g.setColor(LCD_LIGHT);
            g.fillRect(px, py, CELL, CELL);

            // faint grid
            g.setColor(new Color(0, 0, 0, 25));
            g.drawRect(px, py, CELL, CELL);
            return;
        }

        g.setColor(LCD_DARK);
        g.fillRect(px, py, CELL, CELL);

        g.setColor(LCD_DEEP);
        g.drawRect(px, py, CELL, CELL);

        g.setColor(LCD_MID);
        g.drawLine(px + 1, py + 1, px + CELL - 2, py + 1);
        g.drawLine(px + 1, py + 1, px + 1, py + CELL - 2);
    }

    private void drawHud(Graphics g, int x, int y, int w, int h) {
        g.setColor(new Color(184, 204, 150));
        g.fillRect(x, y, w, h);

        g.setColor(LCD_DEEP);
        g.drawRect(x, y, w - 1, h - 1);

        ScoreManager s = game.getScore();

        int tx = x + 12;
        int ty = y + 28;

        g.setColor(LCD_DEEP);

        g.setFont(titleFont);
        g.drawString("SCORE", tx, ty); ty += 18;

        g.setFont(valueFont);
        g.drawString(String.valueOf(s.getScore()), tx, ty); ty += 26;

        g.setFont(titleFont);
        g.drawString("LINES", tx, ty); ty += 18;

        g.setFont(valueFont);
        g.drawString(String.valueOf(s.getLines()), tx, ty); ty += 26;

        g.setFont(titleFont);
        g.drawString("LEVEL", tx, ty); ty += 18;

        g.setFont(valueFont);
        g.drawString(String.valueOf(s.getLevel()), tx, ty); ty += 28;

        // NEXT preview
        g.setFont(titleFont);
        g.drawString("NEXT", tx, ty); ty += 14;

        int boxX = x + 18;
        int boxY = ty + 6;
        int boxSize = 70;

        g.setColor(new Color(210, 225, 180));
        g.fillRect(boxX, boxY, boxSize, boxSize);
        g.setColor(LCD_DEEP);
        g.drawRect(boxX, boxY, boxSize, boxSize);

        int size = 14;
        int px0 = boxX + 14;
        int py0 = boxY + 14;

        game.getNext().forEachPreviewBlock((px, py, colorIdx) -> {
            int ox = px0 + px * size;
            int oy = py0 + py * size;

            g.setColor(LCD_DARK);
            g.fillRect(ox, oy, size, size);
            g.setColor(LCD_DEEP);
            g.drawRect(ox, oy, size, size);
        });

        int infoY = y + h - 74;
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.setColor(LCD_DEEP);
        g.drawString("P PAUSE", tx, infoY); infoY += 14;
        g.drawString("R RESTART", tx, infoY); infoY += 14;
        g.drawString("↑ ROTATE", tx, infoY); infoY += 14;
        g.drawString("SPACE DROP", tx, infoY); infoY += 14;
        g.drawString("ERP Collective <3", tx, infoY);

    }

    private void drawStateOverlay(Graphics g, int x, int y, int w, int h, String state) {
        g.setColor(new Color(0, 0, 0, 90));
        g.fillRect(x, y, w, h);

        g.setFont(stateFont);
        int msgW = g.getFontMetrics().stringWidth(state);
        int cx = x + (w - msgW) / 2;
        int cy = y + h / 2;

        g.setColor(LCD_LIGHT);
        g.drawString(state, cx + 2, cy + 2);
        g.setColor(Color.BLACK);
        g.drawString(state, cx, cy);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        game.onKeyPressed(e.getKeyCode());
        this.timer.setDelay(game.getScore().getDelayMs());
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
