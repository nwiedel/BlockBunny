package de.nicolas.states;

import static de.nicolas.config.GameConfig.PPM;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.nicolas.config.GameConfig;
import de.nicolas.handlers.GameStateManager;
import de.nicolas.handlers.MyContactListener;
import de.nicolas.handlers.MyInput;

public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera b2dCamera;

    private Body playerBody;
    private MyContactListener contactListener;

    public Play(GameStateManager gsm){
        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);

        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

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
        fdef.filter.categoryBits = GameConfig.BIT_GROUND;
        fdef.filter.maskBits = GameConfig.BIT_PLAYER;
        body.createFixture(fdef).setUserData("ground");

        // fallender Player
        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(5 / PPM, 5 / PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = GameConfig.BIT_PLAYER;
        fdef.filter.maskBits = GameConfig.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("player");

        // kreiere foot sensor
        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -5 / PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = GameConfig.BIT_PLAYER;
        fdef.filter.maskBits = GameConfig.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");

        // Box2D Camera
        b2dCamera = new OrthographicCamera();
        b2dCamera.setToOrtho(false,
            GameConfig.V_WIDTH / PPM, GameConfig.V_HEIGHT / PPM);
    }

    @Override
    public void handleInput() {
        // Spieler springt
        if (MyInput.isPressed(MyInput.BUTTON2)){
            if (contactListener.isPlayerOnGround()){
                playerBody.applyForceToCenter(0, 200, true);
            }
        }
    }

    @Override
    public void update(float delta) {

        handleInput();

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
