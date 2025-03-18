package com.example.mistakes_quizlets;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author chaochaowu
 * @Description : MainPresenter
 * @class : MainPresenter
 * @time Create at 6/4/2018 4:21 PM
 */


public class MainPresenter implements MainContract.Presenter{

    private MainContract.View mView;
    private BaiduOCRService baiduOCRService;

    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String API_KEY = "XGf5kIMC2Y8xN93G4QRZsKAx";
    private static final String SECRET_KEY = "Nh3zdDdCkqjTT7edtEZO5g2RL5jUbdc9";
    private static final String ACCESS_TOKEN = "24.dcc01f3a977dca53d6ee71c58ec4b7f1.2592000.1670926962.282335-28390306";
//    private static String ACCESS_TOKEN;


    public MainPresenter(MainContract.View mView) {

        this.mView = mView;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        baiduOCRService = retrofit.create(BaiduOCRService.class);
//
//        ACCESS_TOKEN = AccessToken.getAuth();
//        Log.e("AccessToken",ACCESS_TOKEN);

    }


    @Override
    public void getAccessToken() {

        baiduOCRService.getAccessToken(CLIENT_CREDENTIALS,API_KEY,SECRET_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccessTokenBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AccessTokenBean accessTokenBean) {
//                        Log.e("Access token",accessTokenBean.getAccess_token());
//                        ACCESS_TOKEN = accessTokenBean.getAccess_token();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Access token","error");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    @Override
    public void getRecognitionResultByImage(Bitmap bitmap) {

        String encodeResult = bitmapToString(bitmap);

        baiduOCRService.getRecognitionResultByImage(ACCESS_TOKEN,encodeResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecognitionResultBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(RecognitionResultBean recognitionResultBean) {
                        Log.e("onnext",recognitionResultBean.toString());
                        StringBuilder s = new StringBuilder();
                        List<RecognitionResultBean.WordsResultBean> wordsResult = recognitionResultBean.getWords_result();
                        for (RecognitionResultBean.WordsResultBean words:wordsResult) {
                            s.append(words.getWords());
                        }

                        mView.updateUI(s.toString());

//
//                        ArrayList<String> wordList = new ArrayList<>();
//                        List<RecognitionResultBean.WordsResultBean> wordsResult = recognitionResultBean.getWords_result();
//                        for (RecognitionResultBean.WordsResultBean words:wordsResult) {
//                            wordList.add(words.getWords());
//                        }
//
//                        ArrayList<String> numbs = RegexUtils.getNumbs(wordList);
//                        StringBuilder s = new StringBuilder();
//
//                        for (String numb : numbs) {
//                            s.append(numb + "\n");
//                        }
//
//                        mView.updateUI(s.toString());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onerror",e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    private String bitmapToString(Bitmap bitmap){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        Log.e("encode_result",Base64.encodeToString(bytes, Base64.DEFAULT));
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


}