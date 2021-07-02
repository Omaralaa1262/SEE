package com.example.SEE.FacialAuthentication;


import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class registrationHandler extends AsyncTask<String, String,String> {
    String id;
    protected String doInBackground(String... names) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "name="+names[0]+"%20"+names[1]);

        Request request = new Request.Builder()
                .url("https://luxand-cloud-face-recognition.p.rapidapi.com/subject")
                .post(body)
                .addHeader("x-rapidapi-host", "luxand-cloud-face-recognition.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "ff744549f41842e09ae35822343ab91c")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().toString());
            JSONObject obj = new JSONObject(response.body().string());
            id = obj.getString("id");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(names[0]+" "+names[1]);
        return id;

    }

}

