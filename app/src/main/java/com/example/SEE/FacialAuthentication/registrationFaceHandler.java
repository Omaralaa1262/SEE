package com.example.SEE.FacialAuthentication;
///add photo

import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class registrationFaceHandler extends AsyncTask<String, String,String> {
    protected String doInBackground(String... names) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "photo="+names[0]);
        System.out.println(names[0]);

        Request request = new Request.Builder()
                .url("https://luxand-cloud-face-recognition.p.rapidapi.com/subject/"+names[1])
                .post(body)
                .addHeader("x-rapidapi-host", "luxand-cloud-face-recognition.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "ff744549f41842e09ae35822343ab91c")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }

}

