package de.nicolas.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import de.nicolas.config.GameConfig;
import de.nicolas.handlers.GameStateManager;
import de.nicolas.handlers.MyContactListener;
import de.nicolas.handlers.MyInput;

import static de.nicolas.config.GameConfig.PPM;

public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera b2dCamera;

    private Body playerBody;
    private MyContactListener contactListener;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer mapRenderer;

    public Play(GameStateManager gsm){
        super(gsm);

        /////// Box2D Kram ///////
        world = new World(new Vector2(0, -9.81f), true);
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
        debugRenderer = new Box2DDebugRenderer();

        // Player kreieren
        createPlayer();

        // Tiles kreieren
        createTiles();



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

        // Map zeichnen
        mapRenderer.setView(camera);
        mapRenderer.render();

        debugRenderer.render(world, b2dCamera.combined);
    }

    @Override
    public void dispose() {

    }

    private void createPlayer(){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bdef);

        shape.setAsBox(5 / PPM, 5 / PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = GameConfig.BIT_PLAYER;
        fdef.filter.maskBits = GameConfig.BIT_RED;
        playerBody.createFixture(fdef).setUserData("player");

        // kreiere foot sensor
        shape.setAsBox(2 / PPM, 2 / PPM, new Vector2(0, -5 / PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = GameConfig.BIT_PLAYER;
        fdef.filter.maskBits = GameConfig.BIT_RED;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");
    }

    private void createTiles(){

        tiledMap = new TmxMapLoader().load("maps/test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        tileSize = (int)tiledMap.getProperties().get("tilewidth");

        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        createLayer(layer, GameConfig.BIT_RED);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("green");
        createLayer(layer, GameConfig.BIT_GREEN);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        createLayer(layer, GameConfig.BIT_BLUE);
    }

    private void createLayer(TiledMapTileLayer layer, short bits){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (int row = 0; row < layer.getHeight(); row++){
            for (int col = 0; col < layer.getWidth(); col++){
                // Zelle holen
                Cell cell = layer.getCell(col, row);

                // Existenz checken
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // Box2D Body und Fixture erstellen
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(
                    (col + 0.5f) * tileSize / PPM,
                    (row + 0.5f) * tileSize / PPM
                );

                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2(tileSize / 2 / PPM, tileSize  / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = GameConfig.BIT_RED;
                fdef.filter.maskBits = GameConfig.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
            }
        }
    }
}
