package com.example.progeo.capitalsandcountries;

import androidx.fragment.app.Fragment;

import static com.example.progeo.capitalsandcountries.ActivityCapAndCountries.capitalsList;
import static com.example.progeo.capitalsandcountries.ActivityCapAndCountries.countriesList;
import static com.example.progeo.capitalsandcountries.ActivityCapAndCountries.imageIdsList;

public class FrGCountry extends FrGuess {

    public void prepareQuestion() {
        /*
          Выбираем случайным образом 4 страны со столицами
        */
        String[] capitals = new String[4];
        String[] countries = new String[4];
        int[] images = new int[4];
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * countriesList.size());
            countries[i] = countriesList.get(random);
            capitals[i] = capitalsList.get(random);
            images[i] = imageIdsList.get(countriesList.indexOf(countries[i]));

            imageIdsList.remove(countriesList.indexOf(countries[i]));
            countriesList.remove(countries[i]);
            capitalsList.remove(capitals[i]);
        }

        //Достаем из массива случайную столицу угадываемой страны и ставим в заголовок
        int random = (int) (Math.random() * 4);
        String capital = capitals[random];
        title.setText(capital);
        cityImage.setImageResource(images[random]);
        //Достаем из другого массива страну, столицей которой является этот город
        rightChoice = countries[random];
        //Устанавливаем на кнопки тексты стран, из которых будет выбирать игрок
        for (int i = 0; i < 4; i++) {
            radioButtons[i].setText(countries[i]);
        }
    }

    @Override
    public Fragment getNextFragment() {
        return new FrGCountry();
    }
}
