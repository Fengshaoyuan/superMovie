package dev.baofeng.com.supermovie.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import dev.baofeng.com.supermovie.domain.RecentUpdate;
import dev.baofeng.com.supermovie.http.ApiService;
import dev.baofeng.com.supermovie.http.BaseApi;
import dev.baofeng.com.supermovie.presenter.iview.IShare;
import dev.baofeng.com.supermovie.view.GlobalMsg;
import dev.baofeng.com.supermovie.view.MovieDetailActivity;

/**
 * Created by huangyong on 2018/1/26.
 * 获取分享id,根据id搜索对应数据并跳转到指定页面
 */

public class SharePresenter extends BasePresenter<IShare> {

    public SharePresenter(Context context, IShare iview) {
        super(context, iview);
    }

    public void getMovieForShare(String mvId) {
        BaseApi.request(BaseApi.createApi(ApiService.class)
                        .getShare(mvId), new BaseApi.IResponseListener<RecentUpdate>() {
                    @Override
                    public void onSuccess(RecentUpdate result) {
                        if (result.getData().size() > 0) {
                            RecentUpdate.DataBean dataBean = result.getData().get(0);
                            String downImgUrl = dataBean.getDownimgurl();
                            String downTitle = dataBean.getDowndtitle();
                            String downLoadName = dataBean.getDownLoadName();
                            String mv_md5_id = dataBean.getMv_md5_id();
                            String downLoadUrl = dataBean.getDownLoadUrl();
                            String mvDesc = dataBean.getMvdesc();
                            Intent intent = new Intent(context, MovieDetailActivity.class);
                            intent.putExtra(GlobalMsg.KEY_POST_IMG, downImgUrl);
                            intent.putExtra(GlobalMsg.KEY_DOWN_URL, downLoadUrl);
                            intent.putExtra(GlobalMsg.KEY_MOVIE_TITLE, downLoadName);
                            intent.putExtra(GlobalMsg.KEY_MOVIE_DOWN_ITEM_TITLE, downTitle);
                            intent.putExtra(GlobalMsg.KEY_MOVIE_DETAIL, mvDesc);
                            intent.putExtra(GlobalMsg.KEY_MV_ID, mv_md5_id);
                            new Handler().postDelayed(() ->
                                    context.startActivity(intent), 2000);
                        }
                        iview.loadData(result);
                    }

                    @Override
                    public void onFail() {
                    }
                }


        );
    }

    @Override
    public void release() {
        unSubcription();
    }

}
