package dev.baofeng.com.supermovie.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huangyong.downloadlib.db.HistoryDao;
import com.huangyong.downloadlib.domain.HistoryInfo;

import java.util.List;

import app.huangyong.com.common.widget.timeline.itemdecoration.DotItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.baofeng.com.supermovie.R;
import dev.baofeng.com.supermovie.adapter.HistoryAdapter;

/**
 * 观看历史展示、删除
 */
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_his_list)
    RecyclerView rvHisList;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.tv_clear)
    TextView tvClear;

    private HistoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        ButterKnife.bind(this);
        tvClear.setOnClickListener(this);
        initData();
    }

    private void initData() {
        initHistory();
    }

    private void initHistory() {
        HistoryDao dao = HistoryDao.getInstance(getApplicationContext());
        List<HistoryInfo> historyInfos = dao.queryAll();
        if (historyInfos != null && historyInfos.size() > 0) {
            rvHisList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            DotItemDecoration mItemDecoration = new DotItemDecoration
                    .Builder(this)
                    .setOrientation(DotItemDecoration.VERTICAL)//if you want a horizontal item decoration,remember to set horizontal orientation to your LayoutManager
                    .setItemStyle(DotItemDecoration.STYLE_DRAW)
                    .setTopDistance(30)//dp
                    .setItemInterVal(60)//dp
                    .setItemPaddingLeft(20)//default value equals to item interval value
                    .setItemPaddingRight(20)//default value equals to item interval value
                    .setDotColor(getResources().getColor(R.color.homepage_item_press))
                    .setDotRadius(2)//dp
                    .setDotPaddingTop(0)
                    .setDotInItemOrientationCenter(false)//set true if you want the dot align center
                    .setLineColor(Color.WHITE)
                    .setLineWidth(1)//dp
                    .setEndText("END")
                    .setTextColor(Color.WHITE)
                    .setTextSize(10)//sp
                    .setDotPaddingText(2)//dp.The distance between the last dot and the end text
                    .setBottomDistance(40)//you can add a distance to make bottom line longer
                    .create();
            mItemDecoration.setSpanIndexListener((View view, int spanIndex) -> {
                Log.i("Info", "view:" + view + "  span:" + spanIndex);
                //设置item的背景图，其实也可以在item布局文件里搞
                //view.setBackgroundResource(spanIndex == 0 ? R.drawable.pop_left : R.drawable.pop_right);
            });
            rvHisList.addItemDecoration(mItemDecoration);
            adapter = new HistoryAdapter(HistoryActivity.this, historyInfos, clickListener);
            rvHisList.setAdapter(adapter);
        } else {
            Toast.makeText(this, "暂无观看记录哦", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 删除历史记录
     */
    HistoryAdapter.OnItemLongClickListener clickListener = new HistoryAdapter.OnItemLongClickListener() {
        @Override
        public void onItemLongClick(int id, List<HistoryInfo> historyInfos, int position) {

            AlertDialog dialog = new AlertDialog.Builder(HistoryActivity.this)
                    .setIcon(R.mipmap.icon)//设置标题的图片
                    .setTitle("提示！")//设置对话框的标题
                    .setMessage("是否删除本条记录")//设置对话框的内容
                    //设置对话框的按钮
                    .setNegativeButton("取消", (DialogInterface dialogInterface, int which) ->
                            dialogInterface.dismiss()
                    )
                    .setPositiveButton("确定", (DialogInterface dialogInterface, int which) -> {

                        adapter.notifyDataSetChanged();
                        HistoryDao dao = HistoryDao.getInstance(getApplicationContext());
                        dao.delete(id);
                        dialogInterface.dismiss();
                        historyInfos.remove(position);
                        adapter.notifyDataSetChanged();

                    }).create();
            dialog.show();
        }
    };

    /**
     * 删除历史记录
     *
     * @param v 组件
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_clear) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setIcon(R.mipmap.icon)
                    .setTitle("提示！")
                    .setMessage("是否清空历史记录")
                    //设置对话框的按钮
                    .setNegativeButton("取消", (DialogInterface dialogInterface, int which) ->
                            dialogInterface.dismiss())
                    .setPositiveButton("确定", (DialogInterface dialogInterface, int which) -> {
                        HistoryDao dao = HistoryDao.getInstance(getApplicationContext());
                        dao.deleteAll();
                        if (adapter != null) {
                            adapter.clear();
                            adapter.notifyDataSetChanged();
                        }
                        dialogInterface.dismiss();
                    }).create();
            dialog.show();
        }
    }
}
