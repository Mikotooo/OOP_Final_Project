package tetris.states;

import tetris.core.Game;

public class GameOverState implements GameState {
    private final Game game;

    public GameOverState(Game game) {
        this.game = game;
    }

    @Override
    public void tick() {
    }

    @Override
    public void onKeyPressed(int keyCode) {
        game.handleGameOverKey(keyCode);
    }

    @Override
    public String name() {
        return "GAME OVER";
    }
}
