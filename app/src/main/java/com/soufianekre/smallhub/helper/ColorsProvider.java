package com.soufianekre.smallhub.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.soufianekre.smallhub.SmallHubApp;
import com.soufianekre.smallhub.data.models.LanguageColorModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class ColorsProvider {

    // Predefined languages.
    private static List<String> POPULAR_LANG = Stream.of("Java", "Kotlin", "JavaScript", "Python", "CSS", "PHP",
            "Ruby", "C++", "C", "Go", "Swift", "Dart", "TypeScript", "Scala", "Html").toList();

    private static Map<String, LanguageColorModel> colors = new LinkedHashMap<>();

    @SuppressLint("CheckResult")
    public static void load(Context context) {
        if (colors.isEmpty()) {
            Observable<String> observable = Observable
                    .create(observableEmitter -> {
                        try {
                            Type type = new TypeToken<Map<String, LanguageColorModel>>() {
                            }.getType();
                            InputStream stream = context
                                    .getAssets().open("colors.json");
                            int size = stream.available();

                            Gson gson = new Gson();
                            JsonReader reader =
                                    new JsonReader(new InputStreamReader(stream));

                            colors.putAll(gson.fromJson(reader, type));

                            /*
                            for (String lang : colors.keySet()) {
                                if (!POPULAR_LANG.contains(lang)) {
                                    colors.remove(lang);
                                }
                            }

                             */
                            observableEmitter.onNext("");
                        } catch (IOException e) {
                            e.printStackTrace();
                            observableEmitter.onError(e);
                        }
                        observableEmitter.onComplete();
                    });

            RxHelper.safeObservable(observable)
                    .subscribe(s -> {/**/},
                            Throwable::printStackTrace);

        }
    }

    @NonNull
    public static ArrayList<String> languages() {
        ArrayList<String> lang = new ArrayList<>(Stream.of(colors)
                .filter(value -> value != null && !InputHelper.isEmpty(value.getKey()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new)));
        lang.add(0, "All");
        lang.addAll(1, POPULAR_LANG);
        return lang;
    }

    @Nullable
    public static LanguageColorModel getColor(@NonNull String lang) {
        return colors.get(lang);
    }

    @ColorInt
    public static int getColorAsColor(@NonNull String lang, @NonNull Context context) {
        LanguageColorModel color = getColor(lang);
        int langColor = ColorGenerator.getColor(context, lang);
        if (color != null && !InputHelper.isEmpty(color.getColor())) {
            try {
                langColor = Color.parseColor(color.getColor());
            } catch (Exception ignored) {
            }
        }
        return langColor;
    }
}