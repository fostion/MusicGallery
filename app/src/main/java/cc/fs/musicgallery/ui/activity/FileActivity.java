package cc.fs.musicgallery.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.fs.musicgallery.Config;
import cc.fs.musicgallery.R;
import cc.fs.musicgallery.ui.adapter.FileAdapter;
import cc.fs.musicgallery.utils.Store;

/**
 * Created by fostion on 2015/12/4.
 */
public class FileActivity extends AppCompatActivity {

    public static final int FLAG_SETTING_IMAGE = 10001;
    public static final int FLAG_SETTING_MUSIC = 10002;

    private List<File> selectedFile;
    private List<File> files;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private LayoutManager layoutManager;
    private FileAdapter adapter;
    private String rootPath = "/mnt/sdcard";
    private String storeName;
    private String currentPath;
    private TextView currentTV;


    public static void start(Activity context,int requestCode){
        Intent intent = new Intent(context,FileActivity.class);
        intent.putExtra("flag",requestCode);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_file);

        if(this.getIntent().getIntExtra("flag",10001) == FLAG_SETTING_IMAGE){
            storeName = Config.IMAGS;
        } else {
            storeName = Config.MUSICS;
        }

        setUpToolbar();
        setUpView();
    }

    private void setUpToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
    }

    private void setUpView() {
        files = new ArrayList<>();
        selectedFile = (List<File>) Store.get(storeName);
        if(selectedFile == null)
            selectedFile = new ArrayList<>();
        currentTV = (TextView)findViewById(R.id.currentPath);
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new FileAdapter(files,selectedFile);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File file) {
                if (file.isDirectory()) {
                    files(file.getPath());
                }
            }
        });

        files(rootPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_file,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if (menuId == android.R.id.home) {
            finish();
            return true;
        }

        if (menuId == R.id.menu_done){
            Store.save(storeName,selectedFile);
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void files(String path) {
        try {
            currentPath = path;
            File file = new File(path);
            files.clear();
            files.addAll(Arrays.asList(file.listFiles()));
            adapter.notifyDataSetChanged();
            currentTV.setText("当前路径："+currentPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (rootPath.equals(currentPath)) {
            super.onBackPressed();
        } else {
            File file = new File(currentPath);
            files(file.getParent());
        }
    }
}
