package com.example.daniel.videostreaming.utils.http;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpRequest {

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();


    public String post(String json, String url) throws IOException {

        Log.d("JSON", "Teste2 -> " + json);
        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();

        builder.url(url);

        MediaType mediaType =
                MediaType.parse("multipart/form-data; charset=utf-8");

        RequestBody body = RequestBody.create(mediaType, json);
        builder.post(body);

        Request request = builder.build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

        return jsonDeResposta;

    }

    public String get(String url) throws IOException {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

        Log.d("GET", "RES_GET -> " + jsonDeResposta );

        return jsonDeResposta;
    }

    public String getJ(String json, String url) throws IOException {


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();

        String jsonDeResposta = response.body().string();

        return jsonDeResposta;
    }


    public String post2(String json, String url) throws IOException {

        //Log.d("JSON", "Teste2 -> " + json);

        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";

        RequestBody formBody = new FormBody.Builder()
                .add("json", json)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println("RES -> " + response.body().string());
        return "TEste";

    }

}
