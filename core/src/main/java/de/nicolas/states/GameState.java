package de.nicolas.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.nicolas.BlockBunnyGame;
import de.nicolas.handlers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gsm;
    protected BlockBunnyGame game;

    protected SpriteBatch batch;
    protected OrthographicCamera camera;
    protected OrthographicCamera hudCamera;

    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        game = gsm.game();
        batch = game.getBatch();
        camera = game.getCamera();
        hudCamera = game.getHudCamera();
    }

    public abstract void handleInput();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();
}
