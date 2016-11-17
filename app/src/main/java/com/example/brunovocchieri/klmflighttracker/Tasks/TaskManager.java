package com.example.brunovocchieri.klmflighttracker.Tasks;

import android.content.Context;

/**
 * Created by Bruno Vocchieri on 23/10/2016.
 */
public class TaskManager {

    public interface TaskListener {
        void performTask(final Object responseObject, final String tag);
        void onRequestError();
    }


    private TaskListener taskListener;

    public TaskManager(TaskListener taskListener) {
        this.taskListener = taskListener;
    }

    protected void doRequest(final String url, final Object responseObjectClass, boolean asString, String tag) {

        try {

            new HttpRequestManager(responseObjectClass, taskListener, asString, tag).get(url);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

