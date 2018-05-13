package com.challenge.snake;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public static int maxX = 400;
    public static int maxY = 500;
    public static float unitW = 0;
    public static float unitH = 0;
    public static final int space = 20;
    private SurfaceHolder mSurfaceHolder;


    DrawThread thread;
    public GameView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new DrawThread(mSurfaceHolder, getResources());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopGame();
    }

    public void startGame(){
        if(thread != null && !GameState.getInstance().isRunning() && !thread.isAlive()) {
            stopGame();
            GameState.getInstance().reset();
            thread = new DrawThread(mSurfaceHolder, getResources());
            try {
                thread.start();
            } catch (Exception ex){

            }

        }
    }

    public void stopGame(){
        boolean retry = true;
        GameState.getInstance().stopRunning();
        while(retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
