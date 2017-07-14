package com.example.daniel.videostreaming.utils.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpRequest {

    static String URL = "http://mconfdev.ufjf.br/aplicativo/index.php?";
    static String URLPos = "http://mconfdev.ufjf.br/aplicativo/index.php";

    public String post(String json) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();

        builder.url(URLPos);

        MediaType mediaType =
                MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, json);
        builder.post(body);

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

        return jsonDeResposta;

    }

    public String get() throws IOException {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URL).build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

        return jsonDeResposta;
    }
}
