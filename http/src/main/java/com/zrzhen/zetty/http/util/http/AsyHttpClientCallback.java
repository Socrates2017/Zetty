package com.zrzhen.zetty.http.util.http;


import com.zrzhen.zetty.http.http.Response;

public class AsyHttpClientCallback implements Runnable {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public void run() {

    }
}
