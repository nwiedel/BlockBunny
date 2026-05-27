package de.nicolas;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import de.nicolas.config.GameConfig;
import de.nicolas.handlers.Content;
import de.nicolas.handlers.GameStateManager;
import de.nicolas.handlers.MyInput;
import de.nicolas.handlers.MyInputProcessor;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BlockBunnyGame implements ApplicationListener {

    private static final float STEP = 1 / 60f;
    private float accum;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;

    private GameStateManager gsm;

    public static Content res;

    @Override
    public void create() {

        Gdx.input.setInputProcessor(new MyInputProcessor());

        res = new Content();
        res.loadTexture("images/bunny.png", "bunny");

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameConfig.V_WIDTH, GameConfig.V_HEIGHT);
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, GameConfig.V_WIDTH, GameConfig.V_HEIGHT);

        gsm = new GameStateManager(this);
    }

    @Override
    public void render() {
        // ScreenUtils.clear(GameConfig.CORNFLOWER_BLUE);
        ScreenUtils.clear(Color.BLACK);

        accum += Gdx.graphics.getDeltaTime();
        while (accum >= STEP){
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();
            MyInput.update();
        }
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
