package dev.baofeng.com.supermovie.presenter.iview;

import dev.baofeng.com.supermovie.domain.AppUpdateInfo;

/**
 * @author Huangyong
 * @version 1.0
 * 2018/9/27
 * [修改记录] <br/>
 * 2018/9/27 ：created
 * APP更新回调
 */
public interface IupdateView {
    void noUpdate(String url);

    void updateYes(AppUpdateInfo result);
}
