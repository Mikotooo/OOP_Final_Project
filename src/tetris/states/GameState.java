package tetris.states;

public interface GameState {
    void tick();
    void onKeyPressed(int keyCode);
    String name();
}