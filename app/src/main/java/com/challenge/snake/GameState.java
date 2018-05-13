package com.challenge.snake;

public class GameState {

    private static GameState sGameState;
    private Direction mDirection;
    private int mScore = 0;
    private boolean mRunning;
    private boolean mSnakeOverlaped;
    private boolean mSnakeOutOfField;
    private boolean mFoodExist;
    private Callback mCallback;

    interface Callback{
        void gameRunChange(boolean isRun);
        void gameScoreChange(int score);
    }


    private GameState(){
        reset();
        mRunning = false;
    }

    public static GameState getInstance(){
        if(sGameState == null){
            sGameState = new GameState();
        }
        return sGameState;
    }

    public void reset(){
        mDirection = Direction.Center;
        mScore = 0;
        mRunning = true;
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
        mScore += 1;
    }

    public int getScore(){
        return mScore;
    }

    public void setSnakeOverlaped() {
        mSnakeOverlaped = true;
        mRunning = false;
    }

    public void setSnakeOutOfField(){
        mSnakeOutOfField = true;
        mRunning = false;
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
        if(mCallback != null){
            mCallback.gameRunChange(mRunning);
        }
    }
}
