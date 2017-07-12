package com.example.daniel.videostreaming.utils.http.response;


import com.example.daniel.videostreaming.utils.http.HttpResponseCode;

public abstract class AbstractRequestResponse<T> {

    private final String url;
    private final int code;
    private final T content;

    AbstractRequestResponse(String url, int code, T content) {
        this.url = url;
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return this.code;
    }

    public T getContent() {
        return this.content;
    }

    public String getUrl() {
        return this.url;
    }

    public abstract long getContentSize();

    public long getContentSizeInKBytes() {
        return (getContentSize() / 1024);
    }

    public long getContentSizeInMBytes() {
        return (getContentSizeInKBytes() / 1024);
    }

    public boolean isHTTP200Ok() {
        return (getCode() == HttpResponseCode.HTTP_200_OK);
    }

    public boolean isHTTP404NotFound() {
        return (getCode() == HttpResponseCode.HTTP_404_NOT_FOUND);
    }

    @Override
    public String toString() {
        return "[HTTP Code: " + code + " | Size: " + getContentSizeInKBytes() + " KBytes | URL: " + url + "]";
    }
}
