package ink.girigiri.cream_cake;

import retrofit2.Retrofit;

public class IdeaApi {
    public static <T> T getApiService(Class<T> cls) {
        Retrofit retrofit = RetrofitCreator.getRetrofitBuilder().build();
        return retrofit.create(cls);
    }

}
