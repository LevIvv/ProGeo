package com.example.progeo.flags;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.example.progeo.R;
import com.example.progeo.MainActivity;
import com.example.progeo.SomeAnimations;

public class ActivityGameEnded extends AppCompatActivity implements View.OnClickListener {

    TextView mTVScore, mTVReplic;
    Button mButtonMenu, mButtonTryAgain;

    Animation rotateAnim;
    SomeAnimations someAnimations = new SomeAnimations();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_ended);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/font_nice.ttf");
        mTVScore = findViewById(R.id.textView1);
        mTVScore.setTypeface(t);
        mTVReplic = findViewById(R.id.textView2);
        mTVReplic.setTypeface(t);
        mButtonMenu = findViewById(R.id.button_menu);
        mButtonMenu.setOnClickListener(this);
        mButtonTryAgain = findViewById(R.id.button_tryagain);
        mButtonTryAgain.setOnClickListener(this);

        int totalScore = FragmentFlag.getTotalScore();
        String text1 = getString(R.string.rightChoiseQuantity) + " " + totalScore;
        mTVScore.setText(text1);

        String[] positiveReplics = getResources().getStringArray(R.array.positive_replics);
        String[] negativeReplics = getResources().getStringArray(R.array.negative_replics);

        String replicString;
        if (totalScore >= 5)
            replicString = positiveReplics[(int) (Math.random() * positiveReplics.length)];
        else
            replicString = negativeReplics[(int) (Math.random() * negativeReplics.length)];
        mTVReplic.setText(replicString);

        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.button_resume_anim);
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentFlag.clearLastGameResults();
                Intent toFlagsAgain = new Intent(ActivityGameEnded.this, ActivityFlags.class);
                startActivity(toFlagsAgain);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (getScreenHeight() <= 800) {
            mTVScore.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            mTVReplic.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        }
    }

    @Override
    public void onClick(View button) {
        switch (button.getId()) {
            case R.id.button_tryagain:
                button.startAnimation(rotateAnim);
                button.setClickable(false);
                break;

            case R.id.button_menu:
                someAnimations.animateButtonClick((Button) button);
                toMainMenu();
                break;
        }
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public void toMainMenu() {
        Intent toMainMenu = new Intent(this, MainActivity.class);
        startActivity(toMainMenu);
        overridePendingTransition(R.anim.left_angle_anim_in, R.anim.left_angle_anim_out);
        finish();
    }
}
