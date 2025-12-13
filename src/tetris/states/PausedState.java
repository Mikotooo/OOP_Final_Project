package tetris.states;

import tetris.core.Game;

public class PausedState implements GameState {
    private final Game game;

    public PausedState(Game game) {
        this.game = game;
    }

    @Override
    public void tick() {
    }

    @Override
    public void onKeyPressed(int keyCode) {
        game.handlePausedKey(keyCode);
    }

    @Override
    public String name() {
        return "PAUSED";
    }
}
