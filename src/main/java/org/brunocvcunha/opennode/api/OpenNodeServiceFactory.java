/**
 * Copyright (C) 2017 Bruno Candido Volpato da Cunha (brunocvcunha@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brunocvcunha.opennode.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory for Open Node Service
 * 
 * @author brunovolpato
 *
 */
public class OpenNodeServiceFactory {

    private static final String BASE_URL_V1 = "https://api.opennode.co/v1/";

    public static OpenNodeService buildClient(String accessToken) {
        OkHttpClient httpClient = createClientInterceptor(accessToken);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL_V1)
                .addConverterFactory(GsonConverterFactory.create()).client(httpClient).build();

        OpenNodeService service = retrofit.create(OpenNodeService.class);
        return service;
    }

    public static OkHttpClient createClientInterceptor(String accessToken) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder().header("Authorization", accessToken);
                return chain.proceed(newRequest.build());
            }
        });

        OkHttpClient httpClient = okHttpClientBuilder.build();
        return httpClient;
    }
}
