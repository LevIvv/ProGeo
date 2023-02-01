package com.example.progeo.capitalsandcountries;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.progeo.R;
import com.example.progeo.MainActivity;
import com.example.progeo.SomeAnimations;

public class ActivityGameEnded extends AppCompatActivity implements View.OnClickListener {

    TextView mTVScore, mTVReplic;
    Button buttonMenu, buttonTryAgain;
    Animation rotateAnim;
    SomeAnimations someAnimations;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/font_nice.ttf");

        setContentView(R.layout.activity_game_ended);
        mTVScore = findViewById(R.id.textView1);
        mTVScore.setTypeface(t);
        mTVReplic = findViewById(R.id.textView2);
        mTVReplic.setTypeface(t);
        buttonMenu = findViewById(R.id.button_menu);
        buttonMenu.setOnClickListener(this);
        buttonTryAgain = findViewById(R.id.button_tryagain);
        buttonTryAgain.setOnClickListener(this);

        //Если мультиплеер включен
        if (ActivityCapAndCountries.isMultiplayerEnabled()) {
            //Получение очков игроков
            int player0Score;
            int player1Score;
            if (ActivityCapAndCountries.getReverseState()) {
                player0Score = FrGCountryMulti.getPlayer0Score();
                player1Score = FrGCountryMulti.getPlayer1Score();
            } else {
                player0Score = FrGCapitalMulti.getPlayer0Score();
                player1Score = FrGCapitalMulti.getPlayer1Score();
            }
            String scoresString = player0Score + " : " + player1Score;
            SpannableStringBuilder builder = new SpannableStringBuilder(scoresString);
            ForegroundColorSpan styleRed = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.clrRed));
            builder.setSpan(styleRed, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ForegroundColorSpan styleBlue = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.clrBlue));
            builder.setSpan(styleBlue, 4, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mTVScore.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            mTVScore.setText(builder);

            //Результат игры
            if (player0Score > player1Score)
                mTVReplic.setText(R.string.player1win);
            else if (player0Score == player1Score)
                mTVReplic.setText(R.string.draw12);
            else
                mTVReplic.setText(R.string.player2win);
        }
        //Если выключен
        else {
            int totalScore = FrGuess.getTotalScore();

            String[] positiveReplics = getResources().getStringArray(R.array.positive_replics);
            String[] negativeReplics = getResources().getStringArray(R.array.negative_replics);

            String rightChoiseQuantity = getString(R.string.rightChoiseQuantity) + "\n"
                    + totalScore + "/10";
            mTVScore.setText(rightChoiseQuantity);
            String replicString;
            if (totalScore >= 5)
                replicString = positiveReplics[(int) (Math.random() * positiveReplics.length)];
            else
                replicString = negativeReplics[(int) (Math.random() * negativeReplics.length)];
            mTVReplic.setText(replicString);
        }

        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.button_resume_anim);
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FrGuess.clearLastGameResults();
                toCapAndCountries(ActivityCapAndCountries.getReverseState(), ActivityCapAndCountries.isMultiplayerEnabled());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        someAnimations = new SomeAnimations();

        if (getScreenHeight() <= 800) {
            mTVScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            mTVReplic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        }
    }

    @Override
    public void onClick(View button) {
        if (button.getId() == R.id.button_tryagain) {
            button.startAnimation(rotateAnim);
            button.setClickable(false);
        } else if (button.getId() == R.id.button_menu) {
            someAnimations.animateButtonClick((Button) button);
            toMainMenu();
        }
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    private void toCapAndCountries(boolean isReverse, boolean isMultiplayer) {
        Intent intent = new Intent(this, ActivityCapAndCountries.class);
        intent.putExtra("isReverse", isReverse);
        intent.putExtra("isMultiplayer", isMultiplayer);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void toMainMenu() {
        Intent toMainMenu = new Intent(this, MainActivity.class);
        startActivity(toMainMenu);
        overridePendingTransition(R.anim.left_angle_anim_in, R.anim.left_angle_anim_out);
        finish();
    }
}
