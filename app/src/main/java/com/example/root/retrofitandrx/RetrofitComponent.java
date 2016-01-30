package com.example.root.retrofitandrx;

import android.app.Application;

import com.example.root.retrofitandrx.network.ExternalModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by root on 30/01/16.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        ExternalModule.class,
})
public interface RetrofitComponent {
        void inject(MainActivity mainActivity);

        final class Initializer {
        public static RetrofitComponent init(Application application) {
                return DaggerRetrofitComponent.builder()
                        .applicationModule(new ApplicationModule(application))
                        .build();
        }
    }

}
