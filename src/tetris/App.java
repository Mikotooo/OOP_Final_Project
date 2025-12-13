package tetris;

import javax.swing.SwingUtilities;
import tetris.ui.GameWindow;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow w = new GameWindow();
            w.setVisible(true);
        });
    }
}
