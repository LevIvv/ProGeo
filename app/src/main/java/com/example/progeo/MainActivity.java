package com.example.progeo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.progeo.capitalsandcountries.ActivityCapAndCountries;

import com.example.progeo.flags.ActivityFlags;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    TextView title;

    Button playButton;

    Spinner choiceGameMode;

    Switch swPlayerMode;

    SharedPreferences prefs;

    Class gameClass;

    MediaPlayer mp;

    Animation playAnim, rotateAnim;

    boolean isReverse;

    SomeAnimations animations;

    Typeface titleTypeface;

    boolean isSoundOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Выключение Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Загрузка настроек
        prefs = getSharedPreferences(com.example.progeo.Consts.PREFS, MODE_PRIVATE);

        //Инициализация View
        title = findViewById(R.id.textView_title);
        titleTypeface = Typeface.createFromAsset(getAssets(), "fonts/font_menu_title.ttf");
        title.setTypeface(titleTypeface);

        playButton = findViewById(R.id.button_tryagain);
        Button soundButton = findViewById(R.id.button_sound);

        choiceGameMode = findViewById(R.id.spinner_gameMode);
        swPlayerMode = findViewById(R.id.switch_2players);
        swPlayerMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked)
                    swPlayerMode.setText(R.string.multiPlayerMode);
                else
                    swPlayerMode.setText(R.string.singlePlayerMode);
            }
        });

        //Установка слушателей кнопок
        soundButton.setOnClickListener(this);
        soundButton.setOnLongClickListener(this);


        //Инициализация анимаций
        animations = new SomeAnimations();
        playAnim = AnimationUtils.loadAnimation(this, R.anim.button_play_anim);
        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate360);

        ArrayAdapter<?> adapterGameMode = ArrayAdapter.createFromResource(this, R.array.choiseGameMode, android.R.layout.simple_spinner_item);
        adapterGameMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choiceGameMode.setAdapter(adapterGameMode);
        choiceGameMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos == 2 || pos == 3) {
                    if (swPlayerMode.isEnabled()) {
                        swPlayerMode.setChecked(false);
                        swPlayerMode.setEnabled(false);
                    }
                    return;
                }
                if (!swPlayerMode.isEnabled()) {
                    swPlayerMode.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initPlayButtonListener();
        isSoundOn = prefs.getBoolean(com.example.progeo.Consts.IS_SOUND, true);
        if (isSoundOn) {
            startMusic();
        } else {
            soundButton.setBackgroundResource(R.drawable.sound_button_off);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (prefs.getBoolean(com.example.progeo.Consts.IS_SOUND, true)) {
            if (mp != null)
                mp.start();
            else startMusic();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mp != null)
            mp.pause();
    }

    @Override
    protected void onDestroy() {
        if (mp != null)
            mp.release();
        super.onDestroy();
    }

    private void initPlayButtonListener() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View button) {
                switch (choiceGameMode.getSelectedItemPosition()) {
                    case 0:
                        gameClass = ActivityCapAndCountries.class;
                        break;
                    case 1:
                        gameClass = ActivityCapAndCountries.class;
                        isReverse = true;
                        break;
                    case 2:
                        gameClass = ActivityFlags.class;
                        break;

                }
                playButton.clearAnimation();
                playButton.setScaleX(0.85f);
                playButton.setScaleY(0.85f);
                rotateAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        final Intent toPlay = new Intent(button.getContext(), gameClass);
                        if (gameClass == ActivityCapAndCountries.class) {
                            toPlay.putExtra("isReverse", isReverse);
                            if (swPlayerMode.isChecked()) {
                                toPlay.putExtra("isMultiplayer", true);
                            } else {
                                toPlay.putExtra("isMultiplayer", false);
                            }
                        }
                        startActivity(toPlay);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                playButton.startAnimation(rotateAnim);
                playButton.setOnClickListener(null);
            }
        });
        playButton.setAnimation(playAnim);
    }

    private void startMusic() {
        if (prefs.getBoolean(com.example.progeo.Consts.IS_SOUND, true)) {
            mp = MediaPlayer.create(this, R.raw.main_sound);
            mp.setLooping(true);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }

    @Override
    public void onClick(final View button) {
        //Анимация нажатия кнопки
        animations.animateButtonClick((Button) button);

        //Действия при нажатии
        switch (button.getId()) {
            case R.id.button_sound:
                if (isSoundOn) {
                    isSoundOn = false;
                    prefs.edit().putBoolean(com.example.progeo.Consts.IS_SOUND, isSoundOn).apply();
                    button.setBackgroundResource(R.drawable.sound_button_off);
                    mp.pause();
                } else {
                    isSoundOn = true;
                    prefs.edit().putBoolean(com.example.progeo.Consts.IS_SOUND, isSoundOn).apply();
                    button.setBackgroundResource(R.drawable.sound_button);
                    if (mp != null)
                        mp.start();
                    else startMusic();
                }
                break;


        }
    }

    @Override
    public boolean onLongClick(final View button) {
        switch (button.getId()) {
            case R.id.button_sound:
                Toast.makeText(button.getContext(), R.string.sound, Toast.LENGTH_SHORT).show();
                break;

        }
        return false;
    }
}
