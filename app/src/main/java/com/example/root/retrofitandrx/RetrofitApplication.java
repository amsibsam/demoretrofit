package com.example.root.retrofitandrx;

import android.app.Application;

/**
 * Created by root on 30/01/16.
 */
public class RetrofitApplication extends Application {
    private static RetrofitApplication instance;
    private RetrofitComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        component = RetrofitComponent.Initializer.init(this);
    }

    public static RetrofitApplication getInstance(){
        return instance;
    }

    public static RetrofitComponent getComponent(){
        return instance.component;
    }


}
