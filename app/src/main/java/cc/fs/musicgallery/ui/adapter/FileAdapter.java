package cc.fs.musicgallery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.fs.musicgallery.R;


/**
 * Created by fostion on 2015/11/3.
 */
public class FileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_DIR = 0;
    private final int TYPE_FILE = 1;

    private List<File> list;
    private List<File> selectFile;

    public FileAdapter(List<File> _list,List<File> _selectFile){
        this.list = _list;
        this.selectFile = _selectFile;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_DIR){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dir, parent, false);
            return new DirViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
            return new FileViewHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isDirectory())
            return TYPE_DIR;
        else
            return TYPE_FILE;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  DirViewHolder){
            DirViewHolder vh = (DirViewHolder)holder;
            vh.setData(list.get(position));
        } else{
            FileViewHolder vh = (FileViewHolder)holder;
            vh.setData(list.get(position));
        }
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(File file);
    }

    private void add(List<File> files,File file){
        if(file == null)
            return;
        files.add(file);
    }

    private boolean contain(List<File> files,File file){
        for(File tempFile : files){
            if(tempFile.getPath().equals(file.getPath())){
               return true;
            }
        }
        return false;
    }

    private void remove(List<File> files,File file){
        if(file == null)
            return;
        for(int i=0 ; i<files.size() ; i++){
            File tempFile = files.get(i);
            if(tempFile.getPath().equals(file.getPath())){
                files.remove(i);
                break;
            }
        }
    }


    class DirViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox isSelect;

        public DirViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.fileName);
            isSelect = (CheckBox)view.findViewById(R.id.isSelect);
        }

        public void setData(final File file){
            name.setText(file.getName());
            if(contain(selectFile,file)){
                isSelect.setChecked(true);
            } else {
                isSelect.setChecked(false);
            }
            isSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSelect.isChecked()){
                        add(selectFile,file);
                    } else{
                        remove(selectFile,file);
                    }
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(file);
                    }
                }
            });
        }
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox isSelect;

        public FileViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById(R.id.fileName);
            isSelect = (CheckBox)view.findViewById(R.id.isSelect);
        }

        public void setData(final File file){
            name.setText(file.getName());
            if(contain(selectFile,file)){
                isSelect.setChecked(true);
            } else {
                isSelect.setChecked(false);
            }

            isSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isSelect.isChecked()){
                        add(selectFile,file);
                    } else{
                        remove(selectFile,file);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(file);
                    }
                }
            });
        }
    }

}
