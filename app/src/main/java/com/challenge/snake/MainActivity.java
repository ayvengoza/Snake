package com.challenge.snake;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private GameView mGameView;
    private AccelInteractor mInteractor;
    private DirectionInterface mDi = new DirectionInterface() {
        @Override
        public void toRight() {
            mTextView.setText("Right");
            mGameView.setDirection(Direction.Right);
        }

        @Override
        public void toLeft() {
            mTextView.setText("Left");
            mGameView.setDirection(Direction.Left);
        }

        @Override
        public void toUp() {
            mTextView.setText("Up");
            mGameView.setDirection(Direction.Up);
        }

        @Override
        public void toDown() {
            mTextView.setText("Down");
            mGameView.setDirection(Direction.Down);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.vTextView);
        mGameView = new GameView(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.gameView);
        ll.addView(mGameView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mInteractor = new AccelInteractor(this, mDi);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInteractor.recycle();
    }
}
