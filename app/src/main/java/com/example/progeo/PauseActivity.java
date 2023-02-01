package com.example.progeo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PauseActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonResume, buttonToMenu, buttonSound;
    Handler handler = new Handler();
    SomeAnimations someAnimations = new SomeAnimations();
    Animation rotateAnim;
    TextView title;

    SharedPreferences prefs;

    boolean isSoundOn = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);
        prefs = getSharedPreferences(Consts.PREFS, MODE_PRIVATE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        title = findViewById(R.id.title);
        buttonResume = findViewById(R.id.button_resume);
        buttonToMenu = findViewById(R.id.button_menu);
        buttonSound = findViewById(R.id.button_sound);

        Typeface titleFace = Typeface.createFromAsset(getAssets(), "fonts/font_menu_title.ttf");
        title.setTypeface(titleFace);

        buttonResume.setOnClickListener(this);
        buttonToMenu.setOnClickListener(this);
        buttonSound.setOnClickListener(this);

        isSoundOn = prefs.getBoolean(Consts.IS_SOUND, true);

        if (!isSoundOn) {
            buttonSound.setBackgroundResource(R.drawable.sound_button_off);
        }

        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.button_resume_anim);
        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        onClick(buttonResume);
    }

    @Override
    public void onClick(final View button) {
        switch (button.getId()) {
            case R.id.button_resume:
                button.startAnimation(rotateAnim);
                button.setClickable(false);
                break;

            case R.id.button_menu:
                someAnimations.animateButtonClick((Button) button);
                showDialogAfterClickStop();
                break;

            case R.id.button_sound:
                someAnimations.animateButtonClick((Button) button);
                if (isSoundOn) {
                    isSoundOn = false;
                    button.setBackgroundResource(R.drawable.sound_button_off);
                } else {
                    isSoundOn = true;
                    button.setBackgroundResource(R.drawable.sound_button);
                }
                prefs.edit().putBoolean(Consts.IS_SOUND, isSoundOn).apply();
                break;
        }
    }

    private void showDialogAfterClickStop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.warning)
                .setMessage(R.string.warningProgress)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toMainMenu();
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create()
                .show();
    }

    private void toMainMenu() {
        Intent toMainMenu = new Intent(this, MainActivity.class);
        startActivity(toMainMenu);
        overridePendingTransition(R.anim.left_angle_anim_in, R.anim.left_angle_anim_out);
        finish();
    }
}
