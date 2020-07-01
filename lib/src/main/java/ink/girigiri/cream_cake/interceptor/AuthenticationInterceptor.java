package ink.girigiri.cream_cake.interceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * token 拦截器
 */
public class AuthenticationInterceptor implements Interceptor {
    private String mAuthToken;

    public AuthenticationInterceptor(String token) {
        this.mAuthToken = token;
    }

    public String getToken() {
        return mAuthToken;
    }

    public void setToken(String mAuthToken) {
        this.mAuthToken = mAuthToken;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization",mAuthToken);

        Request request = builder.build();
        return chain.proceed(request);
    }
}
