package com.challenge.snake;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    private SurfaceHolder mSurfaceHolder;
    private Snake mSnake;
    private Food mFood;
    private Canvas mCanvas;
    private Direction direction = Direction.Center;
    private boolean isFirstTime = true;
    private long lastTime;

    public DrawThread(SurfaceHolder surfaceHolder, Resources resources){
        mSurfaceHolder = surfaceHolder;
        mSnake = new Snake();
        mFood = new Food(mSnake);
    }

    @Override
    public void run() {
        GameState.getInstance().startRunning();
        lastTime = System.currentTimeMillis();
        while(GameState.getInstance().isRunning()) {
            update();
            draw();
            checkCollision();
            delay();
        }
    }

    private void update(){
        mSnake.update();
        mFood.update();
    }

    private void draw(){
        if(mSurfaceHolder.getSurface().isValid()) {
            if(isFirstTime){
                GameView.unitW = mSurfaceHolder.getSurfaceFrame().width()/GameView.maxX;
                GameView.unitH = mSurfaceHolder.getSurfaceFrame().height()/GameView.maxY;
                isFirstTime = false;
            }
            try {
                mCanvas = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    mCanvas.drawColor(Color.WHITE);
                    if(mSnake.isOverlap(mFood.getPosition())){
                        mFood.draw(mCanvas);
                        mSnake.draw(mCanvas);
                    } else {
                        mSnake.draw(mCanvas);
                        mFood.draw(mCanvas);
                    }

                }
            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private void checkCollision(){
        Point headSnake = mSnake.getHead();
        Point food = mFood.getPosition();
        if(headSnake.equals(food)){
            GameState.getInstance().incraceScore();
            GameState.getInstance().deleteFood();
            mSnake.incraceBodyLength();
        } else if (headSnake.x > GameView.maxX - 1
                || headSnake.x < 1
                || headSnake.y > GameView.maxY - 1
                || headSnake.y < 1){
            GameState.getInstance().setSnakeOutOfField();
        }
    }

    private void delay(){
        long currentTime = System.currentTimeMillis();
        long period = 350;
        long drawTime = (currentTime - lastTime);
        if(period > drawTime) {
            try {
                sleep(period - drawTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastTime = currentTime;
    }
}
