package com.challenge.snake;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_FRAGMENT_WELCOME = "welcome";
    private static final String TAG_FRAGMENT_GAMEOVER = "game over";
    private static final String KEY_BEST_RESULT = "best_result";
    private static final String PREFERENCES_NAME = "preferences";

    private TextView mTextView;
    private TextView mScoreView;
    private GameView mGameView;

    private AccelInteractor mInteractor;

    private boolean isNewRecord = false;

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
                    if(!GameState.getInstance().isRunning()) {
                        mGameView.startGame();
                    }
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mTextView = (TextView) findViewById(R.id.tvDirection);
        mScoreView = (TextView)findViewById(R.id.tvScore);
        mGameView = new GameView(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.gameView);
        ll.addView(mGameView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameEvent(GameEvent gameEvent){
        switch (gameEvent){
            case Started:
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment welcomeFragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_WELCOME);
                Fragment gameOverFragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_GAMEOVER);
                if (welcomeFragment != null || gameOverFragment != null) {
                    if (welcomeFragment != null) {
                        fragmentManager.beginTransaction().remove(welcomeFragment).commit();
                    }
                    if (gameOverFragment != null){
                        fragmentManager.beginTransaction().remove(gameOverFragment).commit();
                    }
                }
                break;
            case Stoped:
                break;
            case GameOver:
                updateBestResult();
                showGameOverDialog();
                break;
            case ScoreUpdate:
                mScoreView.setText("Score: " + GameState.getInstance().getScore());
                break;
        }
    }

    private void showWelcomeDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        WelcomeDialogFragment df = new WelcomeDialogFragment();
        df.show(fragmentManager, TAG_FRAGMENT_WELCOME);
    }

    private void showGameOverDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        GameOverFragment gof = GameOverFragment.newFragment(getBestScore(), getCurrentScore(), isNewRecord);
        gof.show(fragmentManager, TAG_FRAGMENT_GAMEOVER);
    }

    private void updateBestResult(){
        int bestScore = getBestScore();
        int current = getCurrentScore();
        if(bestScore < current){
            putBestScore(current);
            isNewRecord = true;
        } else {
            isNewRecord = false;
        }
    }

    private int getBestScore(){
        SharedPreferences pref = getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        return pref.getInt(KEY_BEST_RESULT, 0);
    }

    private void putBestScore(int bestScore){
        SharedPreferences pref = getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
        pref.edit().putInt(KEY_BEST_RESULT, bestScore).commit();
    }

    private int getCurrentScore(){
        return GameState.getInstance().getScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mInteractor = new AccelInteractor(this, mDi);
        showWelcomeDialog();
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
