package cc.fs.musicgallery.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.fs.musicgallery.Config;
import cc.fs.musicgallery.R;
import cc.fs.musicgallery.ui.service.MusicService;
import cc.fs.musicgallery.utils.Store;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConvenientBanner gallery;
    List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    private void setUpView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        gallery = (ConvenientBanner) findViewById(R.id.gallery);
        images = new ArrayList<>();
        images.clear();
        gallery.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new GalleryHolderView();
            }
        }, images);
        gallery.setPageTransformer(ConvenientBanner.Transformer.CubeOutTransformer);
        gallery.invalidate();
        gallery.startTurning(5000);
        loadCache();

    }

    private void loadCache() {
        List<File> files = (List<File>) Store.get(Config.IMAGS);
        if (files != null) {
            FileAsyncTask asyncTask = new FileAsyncTask(files);
            asyncTask.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.menu_select_image:
                FileActivity.start(this, FileActivity.FLAG_SETTING_IMAGE);
                return true;

            case R.id.menu_select_music:
                FileActivity.start(this, FileActivity.FLAG_SETTING_MUSIC);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (gallery != null) {
            gallery.stopTurning();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (gallery != null) {
            gallery.startTurning(5000);
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FileActivity.FLAG_SETTING_IMAGE:
                    loadCache();
                    break;
                case FileActivity.FLAG_SETTING_MUSIC:
                    Intent intent = new Intent(this,MusicService.class);
                    intent.putExtra("flag",MusicService.UPDATE_MUSIC);
                    startService(intent);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        stopService(new Intent(this,MusicService.class));
        super.onBackPressed();
    }

    public class FileAsyncTask extends AsyncTask<Integer, Integer, ArrayList<String>> {

        private List<File> files;

        private FileAsyncTask(List<File> _files) {
            this.files = _files;
        }

        private boolean isImage(String name){
            if(name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")){
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
                        if (isImage(child.getName())) {
                            imgs.add(child.getAbsolutePath());
                        }
                    }
                } else {
                    if (isImage(file.getName())) {
                        imgs.add(file.getAbsolutePath());
                    }
                }
            }

            return imgs;
        }

        @Override
        protected void onPostExecute(ArrayList<String> imgs) {
            images.clear();
            images.addAll(imgs);
            gallery.notifyDataSetChanged();
        }
    }

    static class GalleryHolderView implements CBPageAdapter.Holder<String> {

        private View view;
        private SimpleDraweeView image;

        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gallery, null);
            image = (SimpleDraweeView) view.findViewById(R.id.image);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int i, String img) {
            image.setImageURI(Uri.parse("file://" + img));
        }
    }
}
