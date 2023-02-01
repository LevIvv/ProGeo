package com.example.progeo.flags;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.progeo.R;
import com.example.progeo.Game;
import com.example.progeo.PauseActivity;
import com.example.progeo.SomeAnimations;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityFlags extends AppCompatActivity implements Game {

    public static ArrayList<String> countriesList;
    public static ArrayList<Integer> flagsList;

    SomeAnimations someAnimations = new SomeAnimations();

    ConstraintLayout root;
    Button mButtonPause, mButtonSound;
    TextView topText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_capcountryflag);
        root = findViewById(R.id.clayout);
        mButtonPause = findViewById(R.id.button_pause);
        mButtonSound = findViewById(R.id.button_sound);
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                someAnimations.animateButtonClick(mButtonPause, eventAfter);
            }
        });
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/font_nice.ttf");
        topText = findViewById(R.id.textview_topText);
        topText.setTypeface(t);
        topText.setText(R.string.scoreQuantity0);
        startGame();
    }

    final SomeAnimations.EventAfterAnimation eventAfter = new SomeAnimations.EventAfterAnimation() {
        @Override
        public void afterAnimation() {
            pauseGame();
        }
    };

    @Override
    public void startGame() {
        countriesList = new ArrayList<>(Arrays.asList("Нидерланды", "Греция", "Сербия", "Германия", "Швейцария", "Бельгия", "Польша", "Ватикан", "Украина", "Великобритания", "Белоруссия", "Россия", "Норвегия", "Франция", "Чехия", "Италия", "Финляндия", "ОАЭ", "Турция", "Казахстан", "Ирак", "Сирия", "Таджикистан", "Армения", "Израиль", "Республика Корея", "Грузия", "Иран", "Япония", "Монголия", "Мадагаскар", "Гвинея", "Бразилия", "Мексика", "Канада", "Новая Зеландия", "Латвия", "Македония", "Болгария", "Швеция", "Эстония", "Индия", "Индонезия", "Пакистан", "Афганистан", "Непал", "КНДР", "Бутан", "Кот-д’Ивуар", "Андорра", "Лихтенштейн", "Мальта", "Австрия", "Литва", "Ирландия", "Хорватия", "Молдавия", "Дания", "Португалия", "Словения", "Люксембург", "Испания", "Монако", "Черногория", "Исландия", "Албания", "Иордания", "Азербайджан", "Таиланд", "Ливан", "Киргизия", "Лаос", "Бангладеш", "Восточный Тимор", "Катар", "Оман", "Мьянма", "Кипр", "КНР", "Камбоджа", "Йемен", "Узбекистан", "Вьетнам", "Кувейт", "Саудовская Аравия", "Нигерия", "Ботсвана", "Колумбия", "Коста-Рика"));
        flagsList = new ArrayList<>(Arrays.asList(R.drawable.fl_niderlandi, R.drawable.fl_greece, R.drawable.fl_serbia, R.drawable.fl_germany, R.drawable.fl_shveitzaria, R.drawable.fl_belgia, R.drawable.fl_polsha, R.drawable.fl_vatican, R.drawable.fl_ukraina, R.drawable.fl_velikobritania, R.drawable.fl_belarus, R.drawable.fl_russia, R.drawable.fl_norway, R.drawable.fl_france, R.drawable.fl_chekh, R.drawable.fl_italy, R.drawable.fl_finland, R.drawable.fl_oae, R.drawable.fl_turkey, R.drawable.fl_kazachstan, R.drawable.fl_irak, R.drawable.fl_siria, R.drawable.fl_tadzhikistan, R.drawable.fl_armenia, R.drawable.fl_israel, R.drawable.fl_korea, R.drawable.fl_gruzia, R.drawable.fl_iran, R.drawable.fl_japan, R.drawable.fl_mongolia, R.drawable.fl_madagaskar, R.drawable.fl_guinea, R.drawable.fl_brazilia, R.drawable.fl_meksika, R.drawable.fl_kanada, R.drawable.fl_newzealand, R.drawable.fl_latvia, R.drawable.fl_makedonia, R.drawable.fl_bolgaria, R.drawable.fl_shveden, R.drawable.fl_estonia, R.drawable.fl_india, R.drawable.fl_indonezia, R.drawable.fl_pakistan, R.drawable.fl_afganistan, R.drawable.fl_nepal, R.drawable.fl_kndr, R.drawable.fl_butan, R.drawable.fl_kotdivuar, R.drawable.fl_andorra, R.drawable.fl_lichtenshtein, R.drawable.fl_malta, R.drawable.fl_austria, R.drawable.fl_litva, R.drawable.fl_iordania, R.drawable.fl_horvatia, R.drawable.fl_moldavia, R.drawable.fl_dania, R.drawable.fl_portugal, R.drawable.fl_slovenia, R.drawable.fl_luxemburg, R.drawable.fl_ispania, R.drawable.fl_monako, R.drawable.fl_chernogoria, R.drawable.fl_islandia, R.drawable.fl_albania, R.drawable.fl_iordania, R.drawable.fl_azerbaidzhan, R.drawable.fl_tailand, R.drawable.fl_livan, R.drawable.fl_kirgizia, R.drawable.fl_laos, R.drawable.fl_bangladesh, R.drawable.fl_vtimor, R.drawable.fl_katar, R.drawable.fl_oman, R.drawable.fl_mianma, R.drawable.fl_kipr, R.drawable.fl_knr, R.drawable.fl_kambodzha, R.drawable.fl_iemen, R.drawable.fl_usbekistan, R.drawable.fl_vientam, R.drawable.fl_kuveit, R.drawable.fl_saudovskaya, R.drawable.fl_nigeria, R.drawable.fl_botsvana, R.drawable.fl_kolumbia, R.drawable.fl_kostarika));
        FragmentFlag.clearLastGameResults();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new FragmentFlag())
                .commit();
    }

    @Override
    public void pauseGame() {
        Intent toPause = new Intent(this, PauseActivity.class);
        startActivity(toPause);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        pauseGame();
    }
}
