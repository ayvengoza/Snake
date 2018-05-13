package com.challenge.snake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private TextView mScoreView;
    private GameView mGameView;
    private AccelInteractor mInteractor;
    private DirectionInterface mDi = new DirectionInterface() {
        @Override
        public void updated(Direction direction) {
            String message = "";
            switch (direction){
                case Right:
                    message = ">";
                    break;
                case Left:
                    message = "<";
                    break;
                case Up:
                    message = "^";
                    break;
                case Down:
                    message = "v";
                    break;
                case Center:
                    message = "O";
                    break;
            }
            mTextView.setText(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tvDirection);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameEvent(GameEvent gameEvent){
        switch (gameEvent){
            case Started:
                Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
                break;
            case Stoped:
                Toast.makeText(this, "Stoped", Toast.LENGTH_SHORT).show();
                break;
            case GameOver:
                Toast.makeText(this, "GameOver", Toast.LENGTH_SHORT).show();
                break;
            case ScoreUpdate:
                Toast.makeText(this, "ScoreUpdated", Toast.LENGTH_SHORT).show();
                mScoreView.setText("Score: " + GameState.getInstance().getScore());
                break;
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
