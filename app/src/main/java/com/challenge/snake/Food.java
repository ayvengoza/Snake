package com.challenge.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Food {
    private Point mPosition;
    private Snake mSnake;

    public Food (Snake snake){
        mSnake = snake;
    }

    public void update(){
        if(!GameState.getInstance().isFoodExist()){
            newPosition();
        }
    }

    private void newPosition(){
        Random random = new Random();
        while (mPosition == null || mSnake.isOverlap(mPosition)){
            int x = 1 + random.nextInt(GameView.maxX - 2);
            int y = 1 + random.nextInt(GameView.maxY - 2);
            x = (1 + random.nextInt((int)(GameView.maxX/GameView.space) - 2)) * GameView.space;
            y = (1 + random.nextInt((int)(GameView.maxY/GameView.space) - 2)) * GameView.space;
            mPosition = new Point(x, y);
        }
        GameState.getInstance().putFood();
    }

    public void draw(Canvas canvas){
        if(mPosition != null){
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(mPosition.x * GameView.unitW,
                    mPosition.y * GameView.unitH,
                    (float) (Math.min(GameView.unitW, GameView.unitH)),
                    paint);
        }
    }

    public Point getPosition(){
        return mPosition;
    }
}
