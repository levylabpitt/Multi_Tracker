package com.synapse;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class UrlRetriever {



    private final OkHttpClient httpClient;

    public UrlRetriever(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }


    public String getRootUrl(String url) {
        try (Response response = httpClient.newCall(buildRequest(url)).execute()) {
            String sourceUrl = response.request().url().toString();

            return sourceUrl;
        } catch (IOException e) {
            System.err.println("Url retrieval failed for " + url + " (returning self), reason: " + e.getMessage());
            return url;
        }
    }


    private Request buildRequest(String url) {
        try {
            return new Request.Builder()
                    .head()
                    // Act like a browser, because Medium (and maybe others) don't redirect correctly otherwise
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
                    .url(url)
                    .build();
        } catch (IllegalArgumentException e) {
            System.err.println("Url retrieval failed for " + url + ". reason: " + e.getMessage());
            throw e;
        }
    }
}