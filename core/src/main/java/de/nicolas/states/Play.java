package de.nicolas.states;

import static de.nicolas.config.GameConfig.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.nicolas.config.GameConfig;
import de.nicolas.handlers.GameStateManager;

public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera b2dCamera;

    public Play(GameStateManager gsm){
        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        debugRenderer = new Box2DDebugRenderer();

        // Platform erstellen
        BodyDef bdef = new BodyDef();
        bdef.position.set(160 / PPM, 120 / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 5 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        // fallende Box erstellen
        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        shape.setAsBox(5 / PPM, 5 / PPM);

        fdef.shape = shape;
        fdef.restitution = 0.5f;
        body.createFixture(fdef);

        // Box2D Camera
        b2dCamera = new OrthographicCamera();
        b2dCamera.setToOrtho(false,
            GameConfig.V_WIDTH / PPM, GameConfig.V_HEIGHT / PPM);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float delta) {
        world.step(delta, 6, 2);
    }

    @Override
    public void render() {
        debugRenderer.render(world, b2dCamera.combined);
    }

    @Override
    public void dispose() {

    }
}
