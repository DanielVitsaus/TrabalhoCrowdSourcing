package com.example.daniel.videostreaming.models;

/**
 * Created by daniel on 28/06/17.
 */

public class Videos {

    private String url_video;
    private String nome_Video;

    public Videos() {
    }

    public Videos(String url_video, String nome_Video) {
        this.url_video = url_video;
        this.nome_Video = nome_Video;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public String getNome_Video() {
        return nome_Video;
    }

    public void setNome_Video(String nome_Video) {
        this.nome_Video = nome_Video;
    }
}
