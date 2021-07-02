package com.example.SEE.FacialAuthentication;
import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import static java.nio.file.attribute.AclEntry.newBuilder;

public class facialLoginHandler extends AsyncTask<String, String,String> {
    String id;
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Toast.makeText(,"Please capture image first using open camera button.", Toast.LENGTH_SHORT).show();
            System.out.println(name);
    }
    String name;
    protected String doInBackground(String... names) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        //MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "photo="+names[0]);

        Request request = new Request.Builder()
                .url("https://api.luxand.cloud/photo/search")
                .method("POST",body)
                .addHeader("token", "ff744549f41842e09ae35822343ab91c")
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONArray jArr= new JSONArray(response.body().string());
            JSONObject obj = new JSONObject(jArr.getJSONObject(0).toString());
            id = obj.getString("id");
            name=obj.getString("name");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;

    }

}

