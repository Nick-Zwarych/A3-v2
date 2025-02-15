package com.example.assignment3.utils;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiClient {
    private static final OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void get(String url, Callback callback){
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    // we are never posting anything back to a server/client in this application - not needed
    // unsure if needed for assignment 3 since we are going to be communicating with firebase

    //public static void post(String url, String json, Callback callback){
    //    RequestBody body = RequestBody.create(JSON, json);
    //    Request request = new Request.Builder().url(url).post(body).build();
    //    client.newCall(request).enqueue(callback);
    //}
}