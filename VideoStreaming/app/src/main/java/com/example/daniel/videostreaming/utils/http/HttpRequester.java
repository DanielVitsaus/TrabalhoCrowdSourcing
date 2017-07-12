package com.example.daniel.videostreaming.utils.http;


import com.example.daniel.videostreaming.utils.http.response.ByteArrayResponse;
import com.example.daniel.videostreaming.utils.http.response.StringResponse;

public interface HttpRequester {

    StringResponse doGetRequest(String url) throws HttpClientException;

    ByteArrayResponse doGetRequestAsByteArray(String url) throws HttpClientException;

    StringResponse doPostRequest(String url, String contentType, PostData postData) throws HttpClientException;
}
