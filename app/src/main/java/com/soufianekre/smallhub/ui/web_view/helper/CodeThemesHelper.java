package com.soufianekre.smallhub.ui.web_view.helper;

import com.annimon.stream.Stream;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.PrefGetter;
import com.soufianekre.smallhub.SmallHubApp;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class CodeThemesHelper {

    // get Themes for assets directory
    @NonNull public static List<String> listThemes() {
        try {
            List<String> list = Stream.of(SmallHubApp.getInstance().getAssets().list("highlight/styles/themes"))
                    .map(s -> "themes/" + s)
                    .toList();
            list.add(0, "prettify.css");
            list.add(1, "prettify_dark.css");
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @NonNull public static String getTheme(boolean isDark) {
        String theme = PrefGetter.getCodeTheme();
        if (InputHelper.isEmpty(theme) || !exists(theme)) {
            return "prettify.css" ;
        }
        return theme;
    }

    private static boolean exists(@NonNull String theme) {
        return listThemes().contains(theme);
    }
}
