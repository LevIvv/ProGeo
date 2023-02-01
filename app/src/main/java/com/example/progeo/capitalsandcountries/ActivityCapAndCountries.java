package com.example.progeo.capitalsandcountries;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.progeo.Game;
import com.example.progeo.PauseActivity;
import com.example.progeo.R;
import com.example.progeo.SomeAnimations;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityCapAndCountries extends AppCompatActivity implements Game {

    private static boolean isReverse;

    private static boolean isMultiplayer;

    public static ArrayList<String> countriesList;
    public static ArrayList<String> capitalsList;
    public static ArrayList<Integer> imageIdsList;

    ConstraintLayout root;

    SomeAnimations animations = new SomeAnimations();

    TextView mTvTopText;

    public static boolean getReverseState() {
        return isReverse;
    }

    public static boolean isMultiplayerEnabled() {
        return isMultiplayer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_capcountryflag);
        root = findViewById(R.id.clayout);
        mTvTopText = findViewById(R.id.textview_topText);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/font_nice.ttf");
        mTvTopText.setTypeface(t);
        isReverse = getIntent().getExtras().getBoolean("isReverse");
        isMultiplayer = getIntent().getExtras().getBoolean("isMultiplayer");

        Button pauseButton = findViewById(R.id.button_pause);

        final SomeAnimations.EventAfterAnimation eventAfter = new SomeAnimations.EventAfterAnimation() {
            @Override
            public void afterAnimation() {
                pauseGame();
            }
        };
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View button) {
                animations.animateButtonClick((Button) button, eventAfter);
            }
        });
        startGame();
    }

    @Override
    public void startGame() {
        countriesList = new ArrayList<>(Arrays.asList("Нидерланды", "Греция", "Сербия", "Германия", "Швейцария", "Бельгия", "Польша", "Ватикан", "Украина", "Великобритания", "Белоруссия", "Россия", "Норвегия", "Франция", "Чехия", "Италия", "Финляндия", "ОАЭ", "Турция", "Казахстан", "Ирак", "Сирия", "Таджикистан", "Армения", "Израиль", "Республика Корея", "Грузия", "Иран", "Япония", "Монголия", "Мадагаскар", "Гвинея", "Бразилия", "Мексика", "Канада", "Новая Зеландия", "Латвия", "Македония", "Болгария", "Швеция", "Эстония", "Индия", "Индонезия", "Пакистан", "Афганистан", "Непал", "КНДР", "Бутан", "Кот-д’Ивуар", "Андорра", "Лихтенштейн", "Мальта", "Австрия", "Литва", "Ирландия", "Хорватия", "Молдавия", "Дания", "Португалия", "Словения", "Люксембург", "Испания", "Монако", "Черногория", "Исландия", "Албания", "Иордания", "Азербайджан", "Таиланд", "Ливан", "Киргизия", "Лаос", "Бангладеш", "Восточный Тимор", "Катар", "Оман", "Мьянма", "Кипр", "КНР", "Камбоджа", "Йемен", "Узбекистан", "Вьетнам", "Кувейт", "Саудовская Аравия", "Нигерия", "Ботсвана", "Колумбия", "Коста-Рика"));
        capitalsList = new ArrayList<>(Arrays.asList("Амстердам", "Афины", "Белград", "Берлин", "Берн", "Брюссель", "Варшава", "Ватикан", "Киев", "Лондон", "Минск", "Москва", "Осло", "Париж", "Прага", "Рим", "Хельсинки", "Абу-Даби", "Анкара", "Астана", "Багдад", "Дамаск", "Душанбе", "Ереван", "Иерусалим", "Сеул", "Тбилиси", "Тегеран", "Токио", "Улан-Батор", "Антананариву", "Конакри", "Бразилиа", "Мехико", "Оттава", "Веллингтон", "Рига", "Скопье", "София", "Стокгольм", "Таллин", "Нью-Дели", "Джакарта", "Исламабад", "Кабул", "Катманду", "Пхеньян", "Тхимпху", "Ямусукро", "Андорра-ла-Велья", "Вадуц", "Валлетта", "Вена", "Вильнюс", "Дублин", "Загреб", "Кишинёв", "Копенгаген", "Лиссабон", "Любляна", "Люксембург", "Мадрид", "Монако", "Подгорица", "Рейкьявик", "Тирана", "Амман", "Баку", "Бангкок", "Бейрут", "Бишкек", "Вьентьян", "Дакка", "Дили", "Доха", "Маскат", "Нейпьидо", "Никосия", "Пекин", "Пномпень", "Сана", "Ташкент", "Ханой", "Эль-Кувейт", "Эр-Рияд", "Абуджа", "Габороне", "Богота", "Сан-Хосе"));
        imageIdsList = new ArrayList<>(Arrays.asList(R.drawable.amsterdam, R.drawable.aphini, R.drawable.belgrad, R.drawable.berlin, R.drawable.bern, R.drawable.brussel, R.drawable.varshava, R.drawable.vatikan, R.drawable.kiev, R.drawable.london, R.drawable.minsk, R.drawable.moskva, R.drawable.oslo, R.drawable.parizh, R.drawable.praha, R.drawable.rim, R.drawable.helsinki, R.drawable.abu_dabi, R.drawable.ankara, R.drawable.astana, R.drawable.bagdad, R.drawable.damask, R.drawable.dushanbe, R.drawable.erevan, R.drawable.ierusalim, R.drawable.seul, R.drawable.tbilisi, R.drawable.tegeran, R.drawable.tokio, R.drawable.ulan_bator, R.drawable.antananarivu, R.drawable.konakry, R.drawable.brazilia, R.drawable.mehico, R.drawable.ottava, R.drawable.vellington, R.drawable.riga, R.drawable.skopie, R.drawable.sofia, R.drawable.stokholm, R.drawable.tallin, R.drawable.new_deli, R.drawable.dzhakarta, R.drawable.islamabad, R.drawable.kabul, R.drawable.katmandu, R.drawable.phenian, R.drawable.thimphu, R.drawable.yamusukro, R.drawable.andorra_la_velia, R.drawable.vaduz, R.drawable.valetta,
                R.drawable.vena, R.drawable.vilnus, R.drawable.dublin, R.drawable.zagreb, R.drawable.kishinev, R.drawable.kopengagen, R.drawable.lissabon, R.drawable.lublyana, R.drawable.luxemburg, R.drawable.madrid, R.drawable.monako, R.drawable.podgorica, R.drawable.rejkiavik, R.drawable.tirana, R.drawable.amman, R.drawable.baku, R.drawable.bangkok, R.drawable.beyrut, R.drawable.bishkek, R.drawable.vientian, R.drawable.dakka, R.drawable.dili, R.drawable.doha, R.drawable.maskat, R.drawable.neipido, R.drawable.nikosiya, R.drawable.pekin, R.drawable.pnompen, R.drawable.sana, R.drawable.tashkent, R.drawable.hanou, R.drawable.el_kuveit, R.drawable.er_riard, R.drawable.abudzha, R.drawable.gaborone, R.drawable.bogota, R.drawable.san_hose));
        if (isMultiplayer) {
            startMultiplayerGame();
        } else {
            startSinglePlayerGame();
        }
        FrGuess.clearLastGameResults();
    }

    private void startSinglePlayerGame() {
        setFragment(isReverse ? new FrGCountry() : new FrGCapital());
        mTvTopText.setText(R.string.scoreQuantity0);
    }

    private void startMultiplayerGame() {
        FrGCapitalMulti.clearLastResults();
        FrGCountryMulti.clearLastResults();
        SpannableStringBuilder emptyScores = new SpannableStringBuilder("Счёт 0 : 0");
        ForegroundColorSpan styleRed = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.clrRed));
        emptyScores.setSpan(styleRed, 5, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ForegroundColorSpan styleBlue = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.clrBlue));
        emptyScores.setSpan(styleBlue, 9, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mTvTopText.setText(emptyScores);
        setFragment(isReverse ? new FrGCountryMulti() : new FrGCapitalMulti());
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void pauseGame() {
        Intent pauseIntent = new Intent(this, PauseActivity.class);
        startActivity(pauseIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        pauseGame();
    }
}
