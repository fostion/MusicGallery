package cc.fs.musicgallery.utils;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

/**
 * Created by fostion on 2015/12/4.
 */
public class Store {
    public static void init(Context context){
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setLogLevel(LogLevel.FULL)
                .build();
    }

    public static void save(String key,Object value){
        Hawk.chain().put(key,value).commit();
    }

    public static Object get(String key){
        Object value = Hawk.get(key);
        return value;
    }
}
