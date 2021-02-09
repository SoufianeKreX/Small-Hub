package com.soufianekre.smallhub.helper;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.BuildConfig;
import com.soufianekre.smallhub.data.network.interceptors.AuthenticationInterceptor;
import com.soufianekre.smallhub.data.network.service.ScrapService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tech.linjiang.pandora.Pandora;

public class JsoupProvider {

    private static OkHttpClient okHttpClient;

    private static OkHttpClient provideOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                client.addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            client.addInterceptor(Pandora.get().getInterceptor());
            client.addInterceptor(new AuthenticationInterceptor(true));
            okHttpClient = client.build();
        }
        return okHttpClient;
    }


    public static ScrapService getTrendingService(@NonNull String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(provideOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ScrapService.class);
    }

    public static ScrapService getWiki() {
        return new Retrofit.Builder()
                .baseUrl("https://github.com/")
                .client(provideOkHttpClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ScrapService.class);
    }
}