package com.challenge.snake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class AccelInteractor {

    private static final String TAG = "AccelInteractor";
    private DirectionInterface mDi;
    private float xPreviousAccel;
    private float yPreviousAccel;
    private float zPreviousAccel;
    private float xAccel;
    private float yAccel;
    private float zAccel;
    private boolean firstUpdate = true;
    private final float shakeThreshold = 1.5f;
    private boolean shakedInitiated = false;
    private SensorManager mSensorManager;
    private Direction lastDirection = Direction.Center;

    SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            updateAccelParameters(event.values[0], event.values[1], event.values[2]);
            Direction direction = getDirection();
            if(!lastDirection.equals(direction)){
                mDi.updated(direction);
                if(direction != Direction.Center){
                    GameState.getInstance().setDirection(direction);
                }
                lastDirection = direction;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public AccelInteractor(Context context, DirectionInterface di){
        mDi = di;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorEventListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    private void updateAccelParameters( float xNewAccel, float yNewAccel, float zNewAccel){
        if(firstUpdate){
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }
        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }

    private boolean isAccelerationChanged(){
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);
        float sqrtDelta = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2));
//        return (deltaX > shakeThreshold && deltaY > shakeThreshold)
//                || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
//                || (deltaY > shakeThreshold && deltaZ > shakeThreshold);
//        return (deltaX > shakeThreshold || deltaY > shakeThreshold || deltaZ > shakeThreshold);
        return sqrtDelta > shakeThreshold;
    }

    private Direction getDirection(){
//        Log.d(TAG, String.format("x:%f y:%f z:%f", xAccel, yAccel, zAccel));
        if(xAccel < -3.5)
            return Direction.Right;
        else if(xAccel > 3.5)
            return Direction.Left;
        else if(yAccel < 1)
            return Direction.Up;
        else if(yAccel > 7)
            return Direction.Down;
        else
            return lastDirection;
//            return Direction.Center;
    }


    public void recycle(){
        mSensorManager.unregisterListener(mSensorEventListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }
}