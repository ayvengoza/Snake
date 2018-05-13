package com.challenge.snake;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected float size;

    public abstract void update();

    public abstract void draw(Paint paint, Canvas canvas);


}
