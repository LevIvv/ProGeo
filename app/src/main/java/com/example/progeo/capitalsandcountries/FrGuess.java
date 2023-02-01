package com.example.progeo.capitalsandcountries;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.progeo.R;
import com.example.progeo.Consts;

import static android.content.Context.VIBRATOR_SERVICE;

public abstract class FrGuess extends Fragment implements View.OnClickListener {

    Animation shakeTitleAnim;
    Animation showButtonNextAnim;
    Animation hideButtonNextAnim;

    boolean isButtonNextShowed;

    //Заголовок и количество ошибок
    TextView title;
    ImageView cityImage;
    TextView topText;
    //Кнопка для перелистывания на следующий фрагмент. Территориально расположена в активити.
    Button buttonNext;
    //Две радиогруппы для радиокнопок и массив 4 радиокнопок. На них устанавливаются случайные тексты городов (стран).
    RadioGroup rg1;
    RadioGroup rg2;
    AppCompatRadioButton[] radioButtons;

    //Строки, используемые для проверки правильности ответа
    String rightChoice;
    String userChoice;

    //Для воспроизведения звуков
    MediaPlayer mp;

    SharedPreferences prefs;

    //Число попыток, очков и ошибок
    private static int attemptQuantity;
    private static int totalScore;

    static void clearLastGameResults() {
        attemptQuantity = 0;
        totalScore = 0;
    }

    public static int getTotalScore() {
        return totalScore;
    }

    public static int getAttemptQuantity() {
        return attemptQuantity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_countries_and_capitals, container, false);
        //Прибавляем количество угадываний всего
        attemptQuantity++;
        prefs = getContext().getSharedPreferences(Consts.PREFS, Context.MODE_PRIVATE);

        title = fragmentView.findViewById(R.id.title);
        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/font_title_candc.ttf"));

        topText = getActivity().findViewById(R.id.textview_topText);

        cityImage = fragmentView.findViewById(R.id.imageViewCity);
        if (getScreenHeight(getContext()) < 900) {
            ViewGroup.LayoutParams params = cityImage.getLayoutParams();
            params.height = 230;
            cityImage.setLayoutParams(params);
            title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        }

        rg1 = fragmentView.findViewById(R.id.rGroup1);
        rg2 = fragmentView.findViewById(R.id.rGroup2);
        radioButtons = new AppCompatRadioButton[]{
                fragmentView.findViewById(R.id.choise1),
                fragmentView.findViewById(R.id.choise2),
                fragmentView.findViewById(R.id.choise3),
                fragmentView.findViewById(R.id.choise4)
        };

        buttonNext = getActivity().findViewById(R.id.button_next);

        rg1.setOnCheckedChangeListener(rgListener1);
        rg2.setOnCheckedChangeListener(rgListener2);
        buttonNext.setOnClickListener(this);
        buttonNext.setEnabled(false);

        showButtonNextAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_next_show);
        hideButtonNextAnim = AnimationUtils.loadAnimation(getContext(), R.anim.button_next_hide);
        shakeTitleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake_title);
        prepareQuestion();

        if (attemptQuantity == 1) buttonNext.startAnimation(hideButtonNextAnim);
        return fragmentView;
    }

    //Подготовка вопроса. Что угадывать: страну или город?
    public abstract void prepareQuestion();

    //обработчик нажатия кнопки "далее"
    @Override
    public void onClick(View buttonNext) {
        //Выключение доступности кнопки "далее"
        buttonNext.startAnimation(hideButtonNextAnim);
        buttonNext.setOnClickListener(null);

        //Находим в массиве со списком столиц столицу, которую выбрал пользователь. Сохраняем её
        for (int i = 0; i < 4; i++) {
            if (radioButtons[i].isChecked()) {
                userChoice = radioButtons[i].getText().toString();
                break;
            }
        }
        showRightChoice();
        //Сравниваем выбор игрока и верную столицу
        if (userChoice.equals(rightChoice)) {
            playSound(R.raw.applause_sound);
            totalScore++;
            String rchString = getString(R.string.scoreQuantity) + " " + getTotalScore();
            topText.setText(rchString);
        }
        //Допущена ошибка: устройство завибрирует
        else {
            showMistake();
        }
        if (attemptQuantity == 10) {
            toEndGameActivity();
        } else {
            toNextFragment();
        }
    }

    //Показ верного ответа
    void showRightChoice() {
        for (AppCompatRadioButton rButton : radioButtons) {
            if (rButton.getText().toString().equals(rightChoice)) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(
                        rButton, "textColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(getContext(), android.R.color.black),
                        ContextCompat.getColor(getContext(), R.color.clrAccent));
                objectAnimator.setDuration(500L).start();
                break;
            }
        }
    }

    //Показ неверного ответа (если таковой был)  =)
    void showMistake() {
        for (AppCompatRadioButton rButton : radioButtons) {
            if (rButton.getText().toString().equals(userChoice)) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofObject(
                        rButton, "textColor",
                        new ArgbEvaluator(),
                        ContextCompat.getColor(getContext(), android.R.color.black),
                        ContextCompat.getColor(getContext(), R.color.clrRed));
                objectAnimator.setDuration(500L).start();
                break;
            }
        }
        //Вибрация
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(150L);
        }
        //Звук ошибки
        playSound(R.raw.error_sound);
        //Запуск анимации тряски заголовка
        title.startAnimation(shakeTitleAnim);
    }

    void playSound(int path) {
        if (prefs.getBoolean(Consts.IS_SOUND, true)) {
            mp = MediaPlayer.create(getContext(), path);
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

    private int getScreenHeight(Context context) {
        int height;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        return height;
    }

    void toEndGameActivity() {
        final Intent toGameEndedActivity = new Intent(getActivity(), ActivityGameEnded.class);
        //Переход в активити окончания игры
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(toGameEndedActivity);
                getActivity().finishAffinity();
                getActivity().overridePendingTransition(R.anim.right_angle_anim_in, R.anim.right_angle_anim_out);
            }
        }, 800L);
    }

    public void toNextFragment() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.container, getNextFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    public abstract Fragment getNextFragment();

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
                    buttonNext.setEnabled(true);
                    buttonNext.startAnimation(showButtonNextAnim);
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
                    buttonNext.setEnabled(true);
                    buttonNext.startAnimation(showButtonNextAnim);
                    isButtonNextShowed = true;
                }
            }
        }
    };
}
