package com.zmx.mian.fragment;

import android.support.v4.app.Fragment;
/**
 * 开发人员：曾敏祥
 * 开发时间：2018-11-16 13:26
 * 类功能：改变vp预加载页面
 */

public abstract class ViewPagerFragment extends Fragment {

    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 定义成抽象的这样子类必须重写此方法
     */
    protected abstract void lazyLoad();
}