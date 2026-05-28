package de.nicolas.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import de.nicolas.BlockBunnyGame;

public class Player extends B2DSprite {

    private int numCrystals;
    private int totalCrystals;

    public Player(Body body) {
        super(body);

        Texture texture = BlockBunnyGame.res.getTexture("bunny");
        TextureRegion[] sprites = TextureRegion.split(texture, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }

    public void collectCrystals(){
        numCrystals++;
    }

    public int getNumCrystals() {
        return numCrystals;
    }

    public void setTotalCrystals(int totalCrystals) {
        this.totalCrystals = totalCrystals;
    }

    public int getTotalCrystals() {
        return totalCrystals;
    }
}
