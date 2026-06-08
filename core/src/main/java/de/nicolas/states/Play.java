package de.nicolas.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import de.nicolas.config.GameConfig;
import de.nicolas.entities.Crystal;
import de.nicolas.entities.HUD;
import de.nicolas.entities.Player;
import de.nicolas.handlers.GameStateManager;
import de.nicolas.handlers.MyContactListener;
import de.nicolas.handlers.MyInput;

import static de.nicolas.config.GameConfig.PPM;

public class Play extends GameState {

    private boolean debug = false;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    private OrthographicCamera b2dCamera;

    private MyContactListener contactListener;

    private TiledMap tiledMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer mapRenderer;

    private Player player;
    private Array<Crystal> crystals;

    private HUD hud;

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

        // Kristalle kreieren
        createCrystals();

        // Box2D Camera
        b2dCamera = new OrthographicCamera();
        b2dCamera.setToOrtho(false,
            GameConfig.V_WIDTH / PPM, GameConfig.V_HEIGHT / PPM);

        // HUD setzen
        hud = new HUD(player);
    }

    @Override
    public void handleInput() {
        // Spieler springt
        if (MyInput.isPressed(MyInput.BUTTON2)){
            if (contactListener.isPlayerOnGround()){
                player.getBody().applyForceToCenter(0, 250, true);
            }
        }

        // Block ändern
        if (MyInput.isPressed(MyInput.BUTTON1)){
            switchBlocks();
        }
    }

    @Override
    public void update(float delta) {

        handleInput();

        world.step(delta, 6, 2);

        // Kristalle entfernen
        Array<Body> bodies = contactListener.getBodiesToRemove();
        for (int i = 0; i < bodies.size; i++){
            Body b = bodies.get(i);
            crystals.removeValue((Crystal)b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystals();
        }
        bodies.clear();

        player.update(delta);

        for (int i = 0; i < crystals.size; i++){
            crystals.get(i).update(delta);
        }
    }

    @Override
    public void render() {

        // Map zeichnen
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        player.render(batch);

        for (int i = 0; i < crystals.size; i++){
            crystals.get(i).render(batch);
        }

        // HUD zeichnen
        batch.setProjectionMatrix(hudCamera.combined);
        hud.render(batch);

        if (debug){
            debugRenderer.render(world, b2dCamera.combined);
        }
    }

    @Override
    public void dispose() {

    }

    private void createPlayer(){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(100 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearVelocity.set(0.1f, 0);
        Body body = world.createBody(bdef);

        shape.setAsBox(13 / PPM, 13 / PPM);

        fdef.shape = shape;
        fdef.filter.categoryBits = GameConfig.BIT_PLAYER;
        fdef.filter.maskBits = GameConfig.BIT_RED;
        body.createFixture(fdef).setUserData("player");

        // kreiere foot sensor
        shape.setAsBox(13 / PPM, 2 / PPM, new Vector2(0, -13 / PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = GameConfig.BIT_PLAYER;
        fdef.filter.maskBits = GameConfig.BIT_RED | GameConfig.BIT_CRYSTAL;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");

        // Player kreieren
        player = new Player(body);

        body.setUserData(player);
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
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = GameConfig.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
            }
        }
    }

    private void createCrystals(){

        crystals = new Array<>();

        MapLayer layer = tiledMap.getLayers().get("crystals");

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject mapObject : layer.getObjects()){

            bdef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mapObject.getProperties().get("x") / PPM;
            float y = (float) mapObject.getProperties().get("y") / PPM;
            bdef.position.set(x, y);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(8 / PPM);

            fdef.shape = circleShape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = GameConfig.BIT_CRYSTAL;
            fdef.filter.maskBits = GameConfig.BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("crystal");

            Crystal crystal = new Crystal(body);
            crystals.add(crystal);

            body.setUserData(crystal);
        }
    }

    private void switchBlocks(){

        Filter filter = player.getBody().getFixtureList().first().getFilterData();
        short bits = filter.maskBits;

        // zur nächsten Farbe wwechseln
        // rot -> grün -> blau -> rot
        if ((bits & GameConfig.BIT_RED) != 0){
            bits &= ~GameConfig.BIT_RED;
            bits |= GameConfig.BIT_GREEN;
        }
        else if ((bits & GameConfig.BIT_GREEN) != 0){
            bits &= ~GameConfig.BIT_GREEN;
            bits |= GameConfig.BIT_BLUE;
        }
        else if ((bits & GameConfig.BIT_BLUE) != 0){
            bits &= ~GameConfig.BIT_BLUE;
            bits |= GameConfig.BIT_RED;
        }

        // Mask bit setzen
        filter.maskBits = bits;
        player.getBody().getFixtureList().first().setFilterData(filter);

        // Mask bit für Foot sensor
        filter = player.getBody().getFixtureList().get(1).getFilterData();
        bits &= ~GameConfig.BIT_CRYSTAL;
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setFilterData(filter);
    }
}
