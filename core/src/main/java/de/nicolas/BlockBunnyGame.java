package de.nicolas;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.nicolas.config.GameConfig;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BlockBunnyGame implements ApplicationListener {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    @Override
    public void create() {
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        ScreenUtils.clear(GameConfig.CORNFLOWER_BLUE);

    }

    /** Getter und Setter */
    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}


}
