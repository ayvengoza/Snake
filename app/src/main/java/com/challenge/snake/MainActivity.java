package com.challenge.snake;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private TextView mScoreView;
    private GameView mGameView;
    private AccelInteractor mInteractor;
    private DirectionInterface mDi = new DirectionInterface() {
        @Override
        public void toRight() {
            mTextView.setText("Right");
            mScoreView.setText(String.valueOf(GameState.getInstance().getScore()));
        }

        @Override
        public void toLeft() {
            mTextView.setText("Left");
            mScoreView.setText(String.valueOf(GameState.getInstance().getScore()));
        }

        @Override
        public void toUp() {
            mTextView.setText("Up");
            mScoreView.setText(String.valueOf(GameState.getInstance().getScore()));
        }

        @Override
        public void toDown() {
            mTextView.setText("Down");
            mScoreView.setText(String.valueOf(GameState.getInstance().getScore()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.vTextView);
        mScoreView = (TextView)findViewById(R.id.tvScore);
        mGameView = new GameView(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.gameView);
        ll.addView(mGameView);
        mGameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGameView.startGame();
            }
        });

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
