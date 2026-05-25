package de.nicolas.handlers;

import de.nicolas.BlockBunnyGame;
import de.nicolas.states.GameState;
import de.nicolas.states.Play;

import java.util.Stack;

public class GameStateManager {

    private BlockBunnyGame game;

    private Stack<GameState> gameStates;

    private static final int PLAY = 123456;

    public GameStateManager(BlockBunnyGame game){
        this.game = game;
        gameStates = new Stack<>();
        pushState(PLAY);
    }

    public void update(float delta){
        gameStates.peek().update(delta);
    }

    public void render(){
        gameStates.peek().render();
    }

    public BlockBunnyGame game(){
        return game;
    }

    private GameState getGameState(int state){
        if(state == PLAY){
            return new Play(this);
        }
        return null;
    }

    public void setState(int state){
        popState();
        pushState(state);
    }

    public void pushState(int state){
        gameStates.push(getGameState(state));
    }

    public void popState(){
        GameState state = gameStates.pop();
        state.dispose();
    }
}
