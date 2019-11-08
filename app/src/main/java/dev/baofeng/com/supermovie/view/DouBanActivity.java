package dev.baofeng.com.supermovie.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import dev.baofeng.com.supermovie.R;
import dev.baofeng.com.supermovie.adapter.DouBanAdapter;
import dev.baofeng.com.supermovie.domain.DoubanTop250;
import dev.baofeng.com.supermovie.presenter.GetDoubanPresenter;
import dev.baofeng.com.supermovie.presenter.iview.IDBTop250;

/**
 * @author Huangyong
 * @version 1.0
 * @ 北京奔流网络信息技术有线公司
 * @ 2018/9/29
 * @ [修改记录] <br/>
 * 2018/9/29 ：created
 */
public class DouBanActivity extends AppCompatActivity implements IDBTop250 {


    @BindView(R.id.rv_favor_list)
    RecyclerView rvFavorList;
    private DouBanAdapter adapter;
    private GetDoubanPresenter presenter;
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.douban_list_layout);
        ButterKnife.bind(this);
    }

    private void initFavorData() {
        presenter = new GetDoubanPresenter(this, this);
        index = 0;
        presenter.getTop250(index * 18, 18);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFavorData();
    }

    @Override
    public void loadData(DoubanTop250 data) {
        rvFavorList.setLayoutManager(new LinearLayoutManager(DouBanActivity.this));
        adapter = new DouBanAdapter(DouBanActivity.this, data);
        rvFavorList.setAdapter(adapter);
    }

    @Override
    public void loadError(String s) {

    }

    @Override
    public void loadMore(DoubanTop250 data) {

    }
}
