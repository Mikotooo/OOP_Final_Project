package tetris.core;

import tetris.states.GameState;
import tetris.states.RunningState;
import tetris.states.PausedState;
import tetris.states.GameOverState;

import java.awt.event.KeyEvent;

public class Game {

    private final Board board;
    private final RandomBag bag;
    private final ScoreManager score;

    private Tetromino active;
    private Tetromino next;

    private GameState runningState;
    private GameState pausedState;
    private GameState gameOverState;

    private GameState state;

    public Game(int w, int h) {
        this.board = new Board(w, h);
        this.bag = new RandomBag();
        this.score = new ScoreManager();

        this.runningState = new RunningState(this);
        this.pausedState = new PausedState(this);
        this.gameOverState = new GameOverState(this);

        reset();
    }

    public void reset() {
        board.clear();
        score.reset();
        active = new Tetromino(bag.next());
        next = new Tetromino(bag.next());
        state = runningState;

        // if spawn collides => game over
        if (!board.canPlace(active)) {
            state = gameOverState;
        }
    }

    public void tick() {
        state.tick();
    }

    public void onKeyPressed(int keyCode) {
        state.onKeyPressed(keyCode);
    }

    public String getStateName() {
        return state.name();
    }

    public Board getBoard() { return board; }
    public ScoreManager getScore() { return score; }
    public Tetromino getActive() { return active; }
    public Tetromino getNext() { return next; }

    /* ---------------- Running behavior ---------------- */

    public void stepGravity() {
        if (tryMove(0, 1)) {
            return;
        }
        // can't move down -> lock and spawn
        lockAndSpawn();
    }

    public void handleRunningKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> tryMove(-1, 0);
            case KeyEvent.VK_RIGHT -> tryMove(1, 0);
            case KeyEvent.VK_DOWN -> {
                if (tryMove(0, 1)) score.onSoftDropStep();
                else lockAndSpawn();
            }
            case KeyEvent.VK_UP -> tryRotateCWWithSimpleKick();
            case KeyEvent.VK_SPACE -> hardDrop();
            case KeyEvent.VK_P -> state = pausedState;
            case KeyEvent.VK_R -> reset();
        }
    }

    public void handlePausedKey(int keyCode) {
        if (keyCode == KeyEvent.VK_P) {
            state = runningState;
        } else if (keyCode == KeyEvent.VK_R) {
            reset();
        }
    }

    public void handleGameOverKey(int keyCode) {
        if (keyCode == KeyEvent.VK_R) {
            reset();
        }
    }

    private boolean tryMove(int dx, int dy) {
        Tetromino test = active.copy();
        test.move(dx, dy);
        if (board.canPlace(test)) {
            active.move(dx, dy);
            return true;
        }
        return false;
    }

    private void tryRotateCWWithSimpleKick() {
        Tetromino test = active.copy();
        test.rotateCW();

        //rotate in place
        if (board.canPlace(test)) {
            active.rotateCW();
            return;
        }

        // simple wall kicks 
        int[] kicks = (active.getType() == ShapeType.I) ? new int[]{-2, -1, 1, 2} : new int[]{-1, 1};

        for (int k : kicks) {
            Tetromino kicked = test.copy();
            kicked.move(k, 0);
            if (board.canPlace(kicked)) {
                active.rotateCW();
                active.move(k, 0);
                return;
            }
        }

        // else rotation fails, do nothing
    }

    private void hardDrop() {
        int dist = 0;
        while (true) {
            Tetromino test = active.copy();
            test.move(0, 1);
            if (!board.canPlace(test)) break;
            active.move(0, 1);
            dist++;
        }
        if (dist > 0) score.onHardDrop(dist);
        lockAndSpawn();
    }

    private void lockAndSpawn() {
        board.lock(active);

        int cleared = board.clearLines();
        score.onLinesCleared(cleared);

        // spawn next
        active = next;
        active.setPosition(3, -2);
        next = new Tetromino(bag.next());

        // check game over
        if (!board.canPlace(active)) {
            state = gameOverState;
        }
    }
}
