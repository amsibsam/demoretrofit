package com.example.root.retrofitandrx.network;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by root on 30/01/16.
 */
@Module
public final class ExternalModule {
    @Provides
    @Singleton
    RetrofitServices provideHaloDocService(Context context) {
        return new RetrofitServices(context);
    }
}

