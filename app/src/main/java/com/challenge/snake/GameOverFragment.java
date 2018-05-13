package com.challenge.snake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class GameOverFragment extends DialogFragment {

    public static final String KEY_BEST_SCORE = "best score";
    public static final String KEY_CURRENT_SCORE = "current score";
    public static final String KEY_IS_RECORD = "is record";

    private TextView mTvBestResult;
    private TextView mTvCurrentResult;

    private int mBestScore;
    private int mCurrentScore;
    private boolean mIsNewRecord;

    public static GameOverFragment newFragment(int bestScore, int currentScore, boolean isNewRecord){
        GameOverFragment fragment = new GameOverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BEST_SCORE, bestScore);
        bundle.putInt(KEY_CURRENT_SCORE, currentScore);
        bundle.putBoolean(KEY_IS_RECORD, isNewRecord);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mBestScore = args.getInt(KEY_BEST_SCORE);
        mCurrentScore = args.getInt(KEY_CURRENT_SCORE);
        mIsNewRecord = args.getBoolean(KEY_IS_RECORD);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_game_over, null);
        mTvBestResult = (TextView) view.findViewById(R.id.tv_score_best_result);
        if(mIsNewRecord){
            mTvBestResult.setText(R.string.string_new_record);
        } else {
            mTvBestResult.setText(String.valueOf(mBestScore));
        }
        mTvCurrentResult = (TextView) view.findViewById(R.id.tv_score_current_result);
        mTvCurrentResult.setText(String.valueOf(mCurrentScore));
        builder.setView(view);
        return builder.create();
    }
}
