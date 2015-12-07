package cc.fs.musicgallery;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import cc.fs.musicgallery.utils.Store;

/**
 * Created by fostion on 2015/12/4.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //init
        Fresco.initialize(this);
        Store.init(this);
    }
}
