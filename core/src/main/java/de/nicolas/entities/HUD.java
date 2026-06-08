package de.nicolas.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.nicolas.BlockBunnyGame;
import de.nicolas.config.GameConfig;

public class HUD {

    private Player player;

    private TextureRegion[] blocks;

    public HUD (Player player){
        this.player = player;

        Texture texture = BlockBunnyGame.res.getTexture("hud");

        blocks = new TextureRegion[3];
        for (int i = 0; i < blocks.length; i++){
            blocks[i] = new TextureRegion(texture, 32 + i * 16, 0 , 16, 16);
        }
    }

    public void render(SpriteBatch batch){
        short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;

        batch.begin();
        if ((bits & GameConfig.BIT_RED) != 0){
            batch.draw(blocks[0], 40, 200);
        }
        if ((bits & GameConfig.BIT_GREEN) != 0){
            batch.draw(blocks[1], 40, 200);
        }
        if ((bits & GameConfig.BIT_BLUE) != 0){
            batch.draw(blocks[2], 40, 200);
        }
        batch.end();
    }

}
