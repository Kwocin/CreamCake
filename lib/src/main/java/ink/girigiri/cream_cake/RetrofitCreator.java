package ink.girigiri.cream_cake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import ink.girigiri.cream_cake.converter.GsonConverterFactory;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitCreator {
    public static OkHttpClient.Builder getOkHttpClientBuilder() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(HttpConfig.getInstance().getApplication().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        return new OkHttpClient.Builder()
                .readTimeout(HttpConfig.getInstance().getTimeOut(), TimeUnit.MILLISECONDS)
                .connectTimeout(HttpConfig.getInstance().getTimeOut(), TimeUnit.MILLISECONDS)
                .addInterceptor(loggingInterceptor)
                .cache(cache);
    }
    public static Retrofit.Builder getRetrofitBuilder() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        OkHttpClient okHttpClient = RetrofitCreator.getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(HttpConfig.getInstance().getHost());
    }
    public static Retrofit.Builder getBaseRetrofitBuilder() {
        OkHttpClient okHttpClient = RetrofitCreator.getOkHttpClientBuilder().build();
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(HttpConfig.getInstance().getHost());
    }
}