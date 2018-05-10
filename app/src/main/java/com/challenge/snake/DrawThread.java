package com.challenge.snake;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    private boolean runFlag = false;
    private boolean firstRun = true;
    private SurfaceHolder mSurfaceHolder;
    private Bitmap picture;
    private Matrix matrix;
    private long prevTime;
    private int x;
    private int y;
    private int step = 1;
    private Direction direction = Direction.Center;

    public DrawThread(SurfaceHolder surfaceHolder, Resources resources){
        mSurfaceHolder = surfaceHolder;
        picture = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);

        matrix = new Matrix();
        matrix.postScale(3.0f, 3.0f);
        matrix.postTranslate(100.0f, 100.0f);

        prevTime = System.currentTimeMillis();
        x = mSurfaceHolder.getSurfaceFrame().width()/2;
        y = mSurfaceHolder.getSurfaceFrame().height()/2;
    }

    public void setRunning(boolean run){
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        canvas = null;
        while(runFlag) {
            try {
                canvas = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    updatePosition();
                    canvas.drawColor(Color.YELLOW);
                    Paint paint = new Paint();
                    paint.setColor(Color.DKGRAY);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(x, y, 20, paint);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    private void updatePosition(){
        switch (direction){
            case Right:
                x+=step;
                break;
            case Left:
                x -= step;
                break;
            case Up:
                y -= step;
                break;
            case Down:
                y += step;
                break;
        }
    }


}
