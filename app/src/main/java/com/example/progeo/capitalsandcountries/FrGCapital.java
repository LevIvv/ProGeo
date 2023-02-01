package com.example.progeo.capitalsandcountries;

import androidx.fragment.app.Fragment;

import static com.example.progeo.capitalsandcountries.ActivityCapAndCountries.capitalsList;
import static com.example.progeo.capitalsandcountries.ActivityCapAndCountries.countriesList;
import static com.example.progeo.capitalsandcountries.ActivityCapAndCountries.imageIdsList;

public class FrGCapital extends FrGuess {

    public void prepareQuestion() {
        /*
          Выбираем случайным образом 4 страны со столицами
        */
        String[] capitals = new String[4];
        String[] countries = new String[4];
        int[] images = new int[4];
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * capitalsList.size());
            countries[i] = countriesList.get(random);
            capitals[i] = capitalsList.get(random);
            images[i] = imageIdsList.get(capitalsList.indexOf(capitals[i]));

            imageIdsList.remove(capitalsList.indexOf(capitals[i]));
            countriesList.remove(countries[i]);
            capitalsList.remove(capitals[i]);
        }

        //Достаем из массива случайную страну, у которой нужно будет угадать столицу, и ставим в заголовок
        int random = (int) (Math.random() * 4);
        String country = countries[random];
        title.setText(country);
        cityImage.setImageResource(images[random]);
        //Достаем из другого массива город-столицу этой страны
        rightChoice = capitals[random];
        //Устанавливаем на кнопки тексты городов, из которых будет выбирать игрок
        for (int i = 0; i < 4; i++) {
            radioButtons[i].setText(capitals[i]);
        }
    }

    @Override
    public Fragment getNextFragment() {
        return new FrGCapital();
    }
}
