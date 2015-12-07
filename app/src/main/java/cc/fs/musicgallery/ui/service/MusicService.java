package cc.fs.musicgallery.ui.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.fs.musicgallery.Config;
import cc.fs.musicgallery.utils.MusicPlayer;
import cc.fs.musicgallery.utils.Store;

/**
 * 背景音乐播放器
 */
public class MusicService extends Service {

    public static final int UPDATE_MUSIC = 10001;

    private MusicPlayer player;
    private List<String> musics;
    private int current = 0;

    public MusicService() {
        musics = new ArrayList<>();
        player = new MusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        List<File> files = (List<File>) Store.get(Config.MUSICS);
        if(files != null){
            MusicAsyncTask asyncTask = new MusicAsyncTask(files);
            asyncTask.execute();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class MusicAsyncTask extends AsyncTask<Integer, Integer, ArrayList<String>> {

        private List<File> files;

        private MusicAsyncTask(List<File> _files) {
            this.files = _files;
        }

        private boolean isMusic(String name) {
            if (name.endsWith(".mp3")) {
                return true;
            }
            return false;
        }

        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            ArrayList<String> imgs = new ArrayList<>();
            for (File file : files) {
                if (file.isDirectory()) {
                    File[] childFile = file.listFiles();
                    for (int i = 0; i < childFile.length; i++) {
                        File child = childFile[i];
                        if (isMusic(child.getName())) {
                            imgs.add(child.getAbsolutePath());
                        }
                    }
                } else {
                    if (isMusic(file.getName())) {
                        imgs.add(file.getAbsolutePath());
                    }
                }
            }

            return imgs;
        }

        @Override
        protected void onPostExecute(ArrayList<String> _musics) {
            musics.clear();
            musics.addAll(_musics);
            if (musics.size() > 0) {
                if (!player.isPlaying()) {
                    current = 0;
                    player.play(musics.get(0));
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            current++;
                            if (current >= musics.size()) {
                                current = 0;
                            }
                            player.play(musics.get(current));
                            Log.e("------","播放完毕，下一首  "+current);
                        }
                    });
                }
            } else {
                if (player.isPlaying()) {
                    player.stop();
                }
            }
        }
    }

    @Override
    public boolean stopService(Intent name) {
        player.stop();
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        player.stop();
        Log.e(" --- ", "结束服务");
        super.onDestroy();
    }
}
