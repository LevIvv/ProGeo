package com.example.progeo.flags;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.progeo.R;
import com.example.progeo.Consts;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;

public class FragmentFlag extends Fragment {

    private TextView topText;
    private ImageView flagPhoto;
    private RadioGroup rg1;
    private RadioGroup rg2;
    private AppCompatRadioButton[] radioButtons = new AppCompatRadioButton[4];
    private Button mButtonNext;

    private static int totalScore;
    private static int attemptQuantity;

    private Animation showNextButtonAnim;
    private Animation hideNextButtonAnim;
    Handler handler = new Handler();
    SharedPreferences prefs;

    private boolean isButtonNextShowed;

    //Страна, флаг которой изображен в фрагменте
    String rightCountry;

    public static int getTotalScore() {
        return totalScore;
    }

    public static void clearLastGameResults() {
        attemptQuantity = 0;
        totalScore = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        View frView = inflater.inflate(R.layout.fragment_flags, root, false);
        prefs = getContext().getSharedPreferences(Consts.PREFS, MODE_PRIVATE);
        attemptQuantity++;
        topText = getActivity().findViewById(R.id.textview_topText);
        flagPhoto = frView.findViewById(R.id.flagPhoto);
        rg1 = frView.findViewById(R.id.rGroup1);
        rg1.setOnCheckedChangeListener(rgListener1);
        rg2 = frView.findViewById(R.id.rGroup2);
        rg2.setOnCheckedChangeListener(rgListener2);
        radioButtons[0] = frView.findViewById(R.id.choise1);
        radioButtons[1] = frView.findViewById(R.id.choise2);
        radioButtons[2] = frView.findViewById(R.id.choise3);
        radioButtons[3] = frView.findViewById(R.id.choise4);

        showNextButtonAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_next_show);
        hideNextButtonAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_next_hide);

        mButtonNext = getActivity().findViewById(R.id.button_next);
        mButtonNext.setEnabled(false);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonNext.setOnClickListener(null);
                mButtonNext.startAnimation(hideNextButtonAnim);
                checkAnswer();
                if (attemptQuantity == 10) {
                    toEndGameActivity();
                } else {
                    toNextFragment();
                }
            }
        });

        prepareQuestion();

        return frView;
    }

    private void prepareQuestion() {
        int rightNumber = (int) (Math.random() * 4);
        //Устанавливаем страны на кнопки
        int[] indexes = new int[4];
        String[] countries = new String[4];
        int[] imageIds = new int[4];
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * ActivityFlags.countriesList.size());
            String randomCountryText = ActivityFlags.countriesList.get(random);
            countries[i] = ActivityFlags.countriesList.get(random);
            indexes[i] = ActivityFlags.countriesList.indexOf(countries[i]);
            imageIds[i] = ActivityFlags.flagsList.get(indexes[i]);
            ActivityFlags.countriesList.remove(countries[i]);
            ActivityFlags.flagsList.remove(indexes[i]);

            //Ставим текст на кнопку
            radioButtons[i].setText(randomCountryText);
        }

        rightCountry = countries[rightNumber];
        flagPhoto.setImageResource(imageIds[rightNumber]);
        indexes = null;
        imageIds = null;
        countries = null;
    }

    private void checkAnswer() {
        for (AppCompatRadioButton radioButton : radioButtons) {
            if (radioButton.getText().toString().equals(rightCountry)) {
                //Показываем, что этот ответ верный
                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(
                        radioButton,
                        "textColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(getContext(), android.R.color.black),
                        ContextCompat.getColor(getContext(), R.color.clrAccent));
                objectAnimator.setDuration(500L).start();

                if (radioButton.isChecked()) {
                    totalScore++;
                    String text = getString(R.string.scoreQuantity) + " " + totalScore;
                    topText.setText(text);
                    playSound(R.raw.applause_sound);
                } else {
                    showMistake();
                    playSound(R.raw.error_sound);
                }
                break;
            }
        }
    }

    private void showMistake() {
        for (AppCompatRadioButton radioButton : radioButtons) {
            if (radioButton.isChecked()) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(
                        radioButton,
                        "textColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(getContext(), android.R.color.black),
                        ContextCompat.getColor(getContext(), R.color.clrRed));
                objectAnimator.setDuration(500L).start();

                Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(150L);
                }
                break;
            }
        }
    }

    void playSound(int path) {
        if (prefs.getBoolean(Consts.IS_SOUND, true)) {
            MediaPlayer mp = MediaPlayer.create(getContext(), path);
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

    private void toNextFragment() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.container, new FragmentFlag())
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void toEndGameActivity() {
        final Intent toGameEndedActivity = new Intent(getActivity(), ActivityGameEnded.class);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(toGameEndedActivity);
                getActivity().overridePendingTransition(R.anim.right_angle_anim_in, R.anim.right_angle_anim_out);
                getActivity().finishAffinity();
            }
        }, 800L);
    }

    //Слушатели для радиогрупп. Две радиогруппы работают, словно одна
    RadioGroup.OnCheckedChangeListener rgListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null);
                rg2.clearCheck();
                rg2.setOnCheckedChangeListener(rgListener2);
                //Заставляем кнопку "далее" появиться, если она не появилась
                if (!isButtonNextShowed) {
                    mButtonNext.setEnabled(true);
                    mButtonNext.startAnimation(showNextButtonAnim);
                    isButtonNextShowed = true;
                }
            }
        }
    };

    RadioGroup.OnCheckedChangeListener rgListener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(rgListener1);
                //Заставляем кнопку "далее" появиться, если она не появилась
                if (!isButtonNextShowed) {
                    mButtonNext.setEnabled(true);
                    mButtonNext.startAnimation(showNextButtonAnim);
                    isButtonNextShowed = true;
                }
            }
        }
    };
}
