package dev.baofeng.com.supermovie.presenter.iview;


import dev.baofeng.com.supermovie.domain.RecentUpdate;

/**
 * creator huangyong
 * createTime 2018/12/8 上午10:47
 * path dev.baofeng.com.supermovie.presenter.iview
 * description:
 */
public interface IRecentView {

    void loadData(RecentUpdate info);

    void loadFail(String s);

    void loadMore(RecentUpdate info);
}
