package dev.baofeng.com.supermovie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.huangyong.downloadlib.model.Params;

import dev.baofeng.com.supermovie.domain.AppUpdateInfo;
import dev.baofeng.com.supermovie.domain.RecentUpdate;
import dev.baofeng.com.supermovie.presenter.SharePresenter;
import dev.baofeng.com.supermovie.presenter.UpdateAppPresenter;
import dev.baofeng.com.supermovie.presenter.iview.IShare;
import dev.baofeng.com.supermovie.presenter.iview.IupdateView;
import app.huangyong.com.common.SharePreferencesUtil;
import dev.baofeng.com.supermovie.view.GlobalMsg;


/**
 * @author Huangyong
 * @version 1.0
 * 启动页 引导页
 * 2018/9/27 ：created
 */
public class SplashActivity extends AppCompatActivity implements IupdateView, IShare {

    private SharePresenter sharePresenter;//分享

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        sharePresenter = new SharePresenter(this, this);
        //应用更新
        UpdateAppPresenter presenter = new UpdateAppPresenter(this, this);
        presenter.getAppUpdate(this);
        initExraData();
    }

    /**
     * 如果来自分享跳转，检查参数请求数据并直接跳转
     */
    private void initExraData() {
        String extra = getIntent().getStringExtra(GlobalMsg.KEY_MV_ID);
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        if (!TextUtils.isEmpty(extra)) {
            sharePresenter.getMovieForShare(extra);
            finish();
        } else {
            new Handler().postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    @Override
    public void noUpdate(String url) {
        SharePreferencesUtil.setIntSharePreferences(SplashActivity.this, Params.HAVE_UPDATE, 0);
    }

    @Override
    public void updateYes(AppUpdateInfo result) {
        SharePreferencesUtil.setIntSharePreferences(SplashActivity.this, Params.HAVE_UPDATE, 1);
    }

    @Override
    public void loadData(RecentUpdate data) {

    }

    @Override
    public void loadFail(String e) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
