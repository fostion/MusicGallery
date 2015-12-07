package cc.fs.musicgallery.utils;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by fostion on 2015/12/5.
 */
public class MusicPlayer {

    private MediaPlayer player;

    public MusicPlayer() {
    }

    public void play(String path) {
        if (player == null)
            player = new MediaPlayer();

        //player music
        try {
            player.reset();
            player.setDataSource(path);
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            player = null;
        }
    }

    public boolean isPlaying() {
        if (player == null)
            return false;
        return player.isPlaying();
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        if (player != null)
            player.setOnCompletionListener(onCompletionListener);
    }

    public void stop() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

}
