package com.example.daniel.videostreaming.utils.http;


import java.util.HashMap;
import java.util.Map;

public class UrlBuilder {

    protected String baseUrl;

    protected HashMap<String, String> queryStringParameters = new HashMap<>();

    public UrlBuilder() {
    }

    public UrlBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public UrlBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public UrlBuilder addOrSetQueryStringParameter(String parameterName, String parameterValue) {
        this.queryStringParameters.put(parameterName, parameterValue);
        return this;
    }

    public String build() {
        return this.baseUrl + generateQueryStringParameters();
    }

    protected String generateQueryStringParameters() {

        String queryString = "?";

        if (this.queryStringParameters.isEmpty()) {
            return "";
        }

        int count = 0;

        for (Map.Entry<String, String> entry : this.queryStringParameters.entrySet()) {

            String queryStringParameter = "";

            if (count < this.queryStringParameters.size()) {
                queryStringParameter = entry.getKey() + "=" + entry.getValue() + "&";
            } else {
                queryStringParameter = entry.getKey() + "=" + entry.getValue();
            }

            queryString += queryStringParameter;
        }

        return queryString;

    }

}
