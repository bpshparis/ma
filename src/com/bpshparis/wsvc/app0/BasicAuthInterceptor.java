package com.bpshparis.wsvc.app0;

import java.io.IOException;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
 * 
 * OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(new BasicAuthInterceptor(username, password))
    .build();
 * 
 */

public class BasicAuthInterceptor implements Interceptor {

    private String credentials;

    public BasicAuthInterceptor(String username, String password) {
        this.credentials = Credentials.basic(username, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build();
        return chain.proceed(authenticatedRequest);
    }

}
