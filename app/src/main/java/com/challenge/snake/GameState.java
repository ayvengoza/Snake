package com.challenge.snake;

import org.greenrobot.eventbus.EventBus;

public class GameState {

    private static GameState sGameState;
    private Direction mDirection;
    private int mScore = 0;
    private boolean mRunning;
    private boolean mSnakeOverlaped;
    private boolean mSnakeOutOfField;
    private boolean mFoodExist;

    private GameState(){
        reset();
    }

    public static GameState getInstance(){
        if(sGameState == null){
            sGameState = new GameState();
        }
        return sGameState;
    }

    public void reset(){
        mDirection = Direction.Center;
        setScore(0);
        mRunning = false;
        mSnakeOverlaped = false;
        mSnakeOutOfField = false;
        mFoodExist = false;
    }

    public Direction getDirection() {
        return mDirection;
    }

    public void setDirection(Direction direction) {
        mDirection = direction;
    }

    public void incraceScore(){
        setScore(mScore + 1);
    }

    public int getScore(){
        return mScore;
    }

    private void setScore(int score){
        mScore = score;
        EventBus.getDefault().post(GameEvent.ScoreUpdate);
    }

    public void setSnakeOverlaped() {
        mSnakeOverlaped = true;
        gameOver();
        stopRunning();
    }

    public void setSnakeOutOfField(){
        mSnakeOutOfField = true;
        gameOver();
        stopRunning();
    }

    public  boolean isFoodExist(){
        return mFoodExist;
    }

    public void putFood(){
        mFoodExist = true;
    }

    public void deleteFood(){
        mFoodExist = false;
    }

    public boolean isRunning(){
        return mRunning;
    }

    public void stopRunning(){
        mRunning = false;
        EventBus.getDefault().post(GameEvent.Stoped);
    }

    public void startRunning(){
        mRunning = true;
        EventBus.getDefault().post(GameEvent.Started);
    }

    private void gameOver(){
        EventBus.getDefault().post(GameEvent.GameOver);
    }
}
