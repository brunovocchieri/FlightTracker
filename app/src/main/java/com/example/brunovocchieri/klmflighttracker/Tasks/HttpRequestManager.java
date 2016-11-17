package com.example.brunovocchieri.klmflighttracker.Tasks;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.OkHttpClient;

/**
 * * Responsible for managing http requests
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class HttpRequestManager implements JSONObjectRequestListener, StringRequestListener {

    private final String BASE_URL = "https://fox.klm.com/fox/json/flightstatuses?";

    private Object                     responseObjectClass;
    private TaskManager.TaskListener   taskListener;
    private String tag;
    private boolean asString;

    public HttpRequestManager(Object responseObjectClass, TaskManager.TaskListener taskListener, boolean asString, String tag) {
        this.responseObjectClass       = responseObjectClass;
        this.taskListener              = taskListener;
        this.asString                  = asString;
        this.tag                       = tag;
    }


    public void get(String url) {
        if (asString){
            AndroidNetworking.get(url)
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(newOkHttpClient())
                    .build()
                    .getAsString(this);
        }
        else{
            AndroidNetworking.get(getAbsoluteUrl(url))
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(newOkHttpClient())
                    .build()
                    .getAsJSONObject(this);
        }

    }


    private OkHttpClient newOkHttpClient(){
        return new OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)
                .build();
    }


    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }



    @Override
    public void onResponse(JSONObject response) {
        Log.w("JSON", response.toString());
        Object responseObject = new Gson().fromJson(response.toString(), responseObjectClass.getClass());

        if(taskListener != null) {
            taskListener.performTask(responseObject, tag);
        }
    }

    @Override
    public void onResponse(String response) {
        Object responseObject = response;

        if(taskListener != null) {
            taskListener.performTask(responseObject, tag);
        }
    }

    @Override
    public void onError(ANError anError) {
        Log.e("REQUEST", "Error: " + anError.getMessage());
        if(taskListener != null) {
            taskListener.onRequestError();
        }
    }
}
