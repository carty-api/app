package com.carty;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Noman on 4/3/2016.
 */
public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                        .applicationId("xQfdLQcHCIqdscBPsDZVE6ASwg53xtK39P0oi7PL")
                        .clientKey("cfN76hgSEj7HZkwFCI0sxNtPEsprRYBzT9OH6fkl")
        .build()
        );
    }
}
