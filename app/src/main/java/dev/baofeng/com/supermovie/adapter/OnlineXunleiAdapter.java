package dev.baofeng.com.supermovie.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyong.downloadlib.TaskLibHelper;
import com.huangyong.downloadlib.model.Params;

import dev.baofeng.com.supermovie.R;
import dev.baofeng.com.supermovie.domain.PlayUrlBean;
import dev.baofeng.com.supermovie.holder.OnlinePlayHolder;
import dev.baofeng.com.supermovie.utils.ToastUtil;

public class OnlineXunleiAdapter extends RecyclerView.Adapter<OnlinePlayHolder> {

    private PlayUrlBean playList;

    private Context context;
    private String poster;

    public OnlineXunleiAdapter(String posterUrl, PlayUrlBean playList) {
        this.playList = playList;
        this.poster = posterUrl;
    }

    @NonNull
    @Override
    public OnlinePlayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_play_item, null);
        return new OnlinePlayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlinePlayHolder holder, int position) {
        if (playList.getNormal().size() == 1) {
            holder.btPlayText.setText("1");
        } else {
            holder.btPlayText.setText(position + 1 + "");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = playList.getXunlei().get(position).getUrl();
                String showText = url.substring(0, 10) + "......"+url.substring(url.length() - 18);
                Snackbar.make(holder.itemView,"即将下载："+showText,Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskLibHelper.addNewTask(playList.getXunlei().get(position).getUrl(), Params.getPath(), poster, context.getApplicationContext());

                        ToastUtil.showMessage("下载任务已添加");
                    }
                }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (playList != null) {
            return playList.getXunlei().size();
        } else {
            return 0;
        }
    }

}
