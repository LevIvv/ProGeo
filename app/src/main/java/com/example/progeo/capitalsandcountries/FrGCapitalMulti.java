package com.example.progeo.capitalsandcountries;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.progeo.R;

public class FrGCapitalMulti extends FrGCapital {

    //Указывает, какой игрок сейчас угадывает
    private static int userNumber;

    //Очки игроков
    private static int player0Score;
    private static int player1Score;

    public static void clearLastResults() {
        player0Score = 0;
        player1Score = 0;
        userNumber = 0;
    }

    public static int getPlayer0Score() {
        return player0Score;
    }

    public static int getPlayer1Score() {
        return player1Score;
    }

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
            switch (userNumber) {
                case 0:
                    player0Score++;
                    break;
                case 1:
                    player1Score++;
                    break;
            }
            String scoresString = "Счёт " + player0Score + " : " + player1Score;
            SpannableStringBuilder builder = new SpannableStringBuilder(scoresString);
            ForegroundColorSpan styleRed = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.clrRed));
            builder.setSpan(styleRed, 5, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ForegroundColorSpan styleBlue = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.clrBlue));
            builder.setSpan(styleBlue, 9, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            topText.setText(builder);
        }
        //Допущена ошибка: устройство завибрирует
        else {
            showMistake();
        }

        //Передаем инициативу другому игроку =)
        switch (userNumber) {
            case 0:
                userNumber = 1;
                break;
            case 1:
                userNumber = 0;
                break;
        }

        if (getAttemptQuantity() == 10) {
            super.toEndGameActivity();
        } else {
            super.toNextFragment();
        }
    }

    @Override
    public Fragment getNextFragment() {
        return new FrGCapitalMulti();
    }
}
