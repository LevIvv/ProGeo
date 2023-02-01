package com.example.progeo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class SomeAnimations {

    public interface EventAfterAnimation {
        void afterAnimation();
    }

    private SharedPreferences prefs;

    //Клик кнопки
    public void animateButtonClick(final Button button) {
        Animation animation = AnimationUtils.loadAnimation(button.getContext(), R.anim.press_button);
        button.startAnimation(animation);

        if (prefs == null)
            prefs = button.getContext().getSharedPreferences(Consts.PREFS, Context.MODE_PRIVATE);
        if (prefs.getBoolean(Consts.IS_SOUND, true)) {
            playButtonClickSound(button.getContext());
        }
    }

    //Клик кнопки с событием после анимации
    public void animateButtonClick(final Button button, final EventAfterAnimation eventAfter) {
        Animation animation = AnimationUtils.loadAnimation(button.getContext(), R.anim.press_button);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                eventAfter.afterAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        button.startAnimation(animation);
        if (prefs == null)
            prefs = button.getContext().getSharedPreferences(Consts.PREFS, Context.MODE_PRIVATE);
        if (prefs.getBoolean(Consts.IS_SOUND, true)) {
            playButtonClickSound(button.getContext());
        }
    }

    //Проигрывание звука
    private void playButtonClickSound(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.button_click_sound);
        mp.setLooping(false);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

}
