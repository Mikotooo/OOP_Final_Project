package tetris.states;

import tetris.core.Game;

public class RunningState implements GameState {
    private final Game game;

    public RunningState(Game game) {
        this.game = game;
    }

    @Override
    public void tick() {
        game.stepGravity();
    }

    @Override
    public void onKeyPressed(int keyCode) {
        game.handleRunningKey(keyCode);
    }

    @Override
    public String name() {
        return "RUNNING";
    }
}
