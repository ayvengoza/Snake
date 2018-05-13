package com.challenge.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> mSnakeBody;
    private int xHead;
    private int yHead;
    private int mBodyLength = 3 * GameView.space - (GameView.space - 1);
    private Point mHead;
    private Direction mDirection = Direction.Up;

    public Snake(){
        mSnakeBody = new LinkedList<>();
        xHead = GameView.maxX/2 + (GameView.maxX/2)%GameView.space;
        for(int i = 0; i<mBodyLength; i++){
            yHead = (GameView.maxY/2 + (GameView.maxY/2)%GameView.space) - i;
            mHead = new Point(xHead, yHead);
            mSnakeBody.addFirst(mHead);
        }
    }

    public void update(){
        updateDirection();
        switch(mDirection){
            case Up:
                yHead -= 1;
                break;
            case Down:
                yHead += 1;
                break;
            case Left:
                xHead -= 1;
                break;
            case Right:
                xHead += 1;
                break;
        }
        Point point = new Point(xHead, yHead);
        if(isOverlap(point)){
            GameState.getInstance().setSnakeOverlaped();
        }
        mHead = new Point(xHead, yHead);
        if(!mSnakeBody.getFirst().equals(mHead)){
            mSnakeBody.addFirst(mHead);
            if(mBodyLength < mSnakeBody.size()) {
                mSnakeBody.removeLast();
            }
        }
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        int counter = 0;
        for(Point point : mSnakeBody){
            if(counter%GameView.space == 0) {
                canvas.drawCircle(point.x * GameView.unitW,
                        point.y * GameView.unitH,
                        (float) (Math.min(GameView.unitW, GameView.unitH)*GameView.space/1.5),
                        paint);
            }
            counter++;
        }
    }

    public void incraceBodyLength(){
        mBodyLength += GameView.space;
    }

    private Direction getDirection(){
        return GameState.getInstance().getDirection();
    }

    private void updateDirection(){
        if(xHead % GameView.space == 0 && yHead % GameView.space == 0) {
            Direction newDirecction = getDirection();
            if (isDirectionAllow(newDirecction) && newDirecction != Direction.Center) {
                mDirection = newDirecction;
            }
        }
    }

    private boolean isDirectionAllow(Direction direction){
        if(mDirection == Direction.Up && direction == Direction.Down)
            return false;
        if(mDirection == Direction.Down && direction == Direction.Up)
            return false;
        if(mDirection == Direction.Right && direction == Direction.Left)
            return false;
        if(mDirection == Direction.Left && direction == Direction.Right)
            return false;
        return true;

    }

    public Point getHead(){
        return mHead;
    }

    public boolean isOverlap(Point point){
        for(Point p : mSnakeBody){
            if(p.equals(point)){
                return true;
            }
        }
        return false;
    }
}
