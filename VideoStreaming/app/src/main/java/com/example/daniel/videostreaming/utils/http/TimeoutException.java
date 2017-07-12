package com.example.daniel.videostreaming.utils.http;


public final class TimeoutException extends HttpClientException {

    public TimeoutException(String url, Throwable cause) {
        super("Timeout durante a leitura dos dados vindos da URL:" + url, cause);
    }

}
