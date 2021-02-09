package com.soufianekre.smallhub.data.network;

import androidx.annotation.NonNull;

import com.soufianekre.quickhub.provider.rest.interceptors.ContentTypeInterceptor;
import com.soufianekre.smallhub.BuildConfig;
import com.soufianekre.smallhub.data.models.GitHubStatusModel;
import com.soufianekre.smallhub.data.network.convertors.GithubResponseConverter;
import com.soufianekre.smallhub.data.network.interceptors.PaginationInterceptor;
import com.soufianekre.smallhub.data.network.service.ContentService;
import com.soufianekre.smallhub.data.network.service.OrganizationService;
import com.soufianekre.smallhub.data.network.service.RepoService;
import com.soufianekre.smallhub.data.network.service.SearchService;
import com.soufianekre.smallhub.data.network.service.UserRestService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.soufianekre.smallhub.helper.GithubApiHelper.REST_URL;

public class RestProvider {

    private static OkHttpClient okHttpClient;

    public final static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();


    public static OkHttpClient provideOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                client.addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            client.addInterceptor(new PaginationInterceptor());
            client.addInterceptor(new ContentTypeInterceptor());
            //client.addInterceptor(Pandora.get().getInterceptor());
            okHttpClient = client.build();
        }
        return okHttpClient;
    }

    private static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                //.baseUrl(LinkParserHelper.getEndpoint(REST_URL))
                .baseUrl(REST_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(new GithubResponseConverter(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    @NonNull
    public static SearchService getSearchService() {
        return provideRetrofit().create(SearchService.class);
    }
    @NonNull
    public static UserRestService getUserRestService() {
        return provideRetrofit().create(UserRestService.class);
    }
    @NonNull
    public static OrganizationService getOrgService() {
        return provideRetrofit().create(OrganizationService.class);
    }
    @NonNull
    public static RepoService getRepoService() {
        return provideRetrofit().create(RepoService.class);
    }


    @NonNull public static UserRestService getContribution() {
        return new Retrofit.Builder()
                .baseUrl(REST_URL)
                .addConverterFactory(new GithubResponseConverter(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UserRestService.class);
    }


    @NonNull public static Observable<GitHubStatusModel> gitHubStatus() {
        return new Retrofit.Builder()
                .baseUrl("https://kctbh9vrtdwd.statuspage.io/")
                .client(provideOkHttpClient())
                .addConverterFactory(new GithubResponseConverter(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ContentService.class)
                .checkStatus();
    }


}
