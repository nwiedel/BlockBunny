package de.nicolas.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.nicolas.handlers.GameStateManager;

public class Play extends GameState {

    private BitmapFont font = new BitmapFont();

    public Play(GameStateManager gsm){
        super(gsm);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "play stat", 100, 100);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
