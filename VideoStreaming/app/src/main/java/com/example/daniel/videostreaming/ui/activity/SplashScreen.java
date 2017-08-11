package com.example.daniel.videostreaming.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.daniel.videostreaming.R;
import com.example.daniel.videostreaming.VideoStreaming;
import com.example.daniel.videostreaming.models.Videos;
import com.example.daniel.videostreaming.utils.http.OkHttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class SplashScreen extends Activity{


    private static final String IDUSER = "IDUSER";

    private Videos videos ;
    private LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        linearLayout = (LinearLayout) findViewById(R.id.linear);

        setContentView(R.layout.activity_splash_screen);

        videos = VideoStreaming.getVideos();

        new ColetaDados().execute("http://mconfdev.ufjf.br/aplicativo/index.php?key=app&req=1");

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void novaActivity(){

        finish();
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public class ColetaDados extends AsyncTask<String, String, String> {

       @Override
        protected String doInBackground(String... params) {
           String reposta = "";
           OkHttpRequest okHttpRequest = new OkHttpRequest();

           try {
               reposta = okHttpRequest.get(params[0]);
           } catch (IOException e) {
               e.printStackTrace();
           }
            Log.d("GET", reposta);
           return reposta;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("code").compareTo("200") == 0) {
                    videos.setNome_Video(jsonObject.getString("nome"));
                    videos.setUrl_video(jsonObject.getString("mpd"));
                }
                else{
                    Snackbar snackbar = Snackbar.make(linearLayout, "Erro ao buscar a lista de videos", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            novaActivity();
        }
    }
}
