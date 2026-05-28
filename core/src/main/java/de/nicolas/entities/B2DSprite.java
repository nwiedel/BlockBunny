package de.nicolas.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import de.nicolas.config.GameConfig;
import de.nicolas.handlers.Animation;

public class B2DSprite {

    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;

    public B2DSprite(Body body){
        this.body = body;
        animation = new Animation();
    }

    public void setAnimation(TextureRegion[] regions, float delay){
        animation.setFrames(regions, delay);
        width = regions[0].getRegionWidth();
        height = regions[0].getRegionHeight();
    }

    public void update(float delta){
        animation.update(delta);
    }

    public void render(SpriteBatch batch){
        batch.begin();
        batch.draw(animation.getFrame(),
            body.getPosition().x * GameConfig.PPM - width / 2,
            body.getPosition().y * GameConfig.PPM - height / 2);
        batch.end();
    }

    public Body getBody() {
        return body;
    }
    public Vector2 getPosition(){
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
