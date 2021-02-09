package com.soufianekre.smallhub.ui.main;

import android.graphics.Color;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.soufianekre.smallhub.data.models.FirebaseTrendingConfigModel;
import com.soufianekre.smallhub.data.models.LanguageColorModel;
import com.soufianekre.smallhub.data.models.TrendingModel;
import com.soufianekre.smallhub.helper.ColorsProvider;
import com.soufianekre.smallhub.helper.JsoupProvider;
import com.soufianekre.smallhub.helper.RxHelper;
import com.soufianekre.smallhub.ui.base.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MainPresenter<V extends MainMvp.View> extends BasePresenter<V> implements MainMvp.Presenter<V> {


    List<TrendingModel> trendingModelsList = new ArrayList<>();

    @Override
    public List<TrendingModel> getTrendingRepos() {
        return trendingModelsList;
    }


    @Override
    public void onCallApi(String lang, String since) {
        FirebaseTrendingConfigModel model = new FirebaseTrendingConfigModel();
        String language = "";
        if (lang.equals("All")) language = "";
        else language = lang.replace(" ", "-").toLowerCase();
        getCompositeDisposable().add(RxHelper
                .getObservable(JsoupProvider.getTrendingService(model.getPathUrl()).getTrending(language, since))
                .doOnSubscribe(disposable -> {
                    getMvpView().showProgress();
                    getMvpView().clearAdapter();
                })
                .flatMap(stringResponse -> RxHelper.getObservable(
                        getTrendingObservable(stringResponse.body(), model)))
                .subscribe(
                        response -> {
                            getMvpView().showMessage("Trending List size :" + trendingModelsList.size());
                            getMvpView().onNotifyAdapter(response);
                        },
                        this::onError,
                        () -> {
                            getMvpView().hideProgress();
                        }
                ));
    }


    private Observable<List<TrendingModel>> getTrendingObservable(String html, FirebaseTrendingConfigModel model) {
        return Observable.fromPublisher(s -> {
            Document document = Jsoup.parse(html, "");
            Elements list = document.select(model.getListName());
            Elements subListTag = list.select(model.getListNameSublistTag());

            trendingModelsList = Stream.of(subListTag).map(element -> {
                String title = element.select(model.getTitle()).text();
                String description = element.select(model.getDescription()).text();
                String trendingLang = element.select(model.getLanguage()).text();
                String stars = element.select(model.getStars()).text();
                String forks = element.select(model.getForks()).text();
                String todayStars = element.select(model.getTodayStars()).text();
                return new TrendingModel(title, description,
                        trendingLang, stars, forks, todayStars);
            }).collect(Collectors.toList());
            s.onNext(trendingModelsList);
            s.onComplete();
        });

    }


    public void onFilterLanguages(String key) {
        getCompositeDisposable().add(
                RxHelper.getObservable(Observable.fromIterable(ColorsProvider.languages()))
                        .doOnSubscribe(disposable -> {
                            getMvpView().clearNavMenu();
                        })
                        .filter(s -> s.toLowerCase().contains(key.toLowerCase()))
                        .doOnNext(this::sendWithColor).subscribe(s -> {
                }, this::onError));
    }

    private void sendWithColor(String param) {
        LanguageColorModel color = ColorsProvider.getColor(param);
        if (color != null) {
            try {
                int lanColor = Color.parseColor(color.color);
                getMvpView().onAppendNavMenuItem(param, lanColor);
            } catch (Exception e) {
                getMvpView().onAppendNavMenuItem(param, Color.LTGRAY);
            }
        } else {
            getMvpView().onAppendNavMenuItem(param, Color.LTGRAY);
        }
    }

    @Override
    public void onItemClick(int position, View v, TrendingModel item) {
        // go To repo Activity
        getMvpView().showMessage("Go To Repo");
        getMvpView().showTrendingRepo(position,item);

    }

    @Override
    public void onItemLongClick(int position, View v, TrendingModel item) {
        getMvpView().showMessage("Trending repo Long Click");
    }
}
