package com.zmx.mian.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zmx.mian.R;
import com.zmx.mian.ui.util.BadgeView;

import java.util.List;


/**
 * 开发人员：曾敏祥
 * 开发时间：2018-10-23 18:43
 * 类功能：tab的适配器
 */

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mFragmentList;     //  对应fragment的集合
    private List<String> mPageTitleList;      //  对应TabLayout对应的标题，与fragment相对应
    private List<Integer> mBadgeCountList;    //  对应TabLayout对应的标题，右上角的数字展示

    public TabFragmentAdapter(Context context,
                              FragmentManager fm,
                              List<Fragment> fragmentList,
                              List<String> pageTitleList,
                              List<Integer> badgeCountList) {
        super(fm);
        this.mContext = context;
        this.mFragmentList = fragmentList;
        this.mPageTitleList = pageTitleList;
        this.mBadgeCountList = badgeCountList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mPageTitleList.get(position);
    }

    /**根据角标获取标题item的布局文件*/
    public View getTabItemView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_layout_item, null);  // 标题布局
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mPageTitleList.get(position));  // 设置标题内容

        View target = view.findViewById(R.id.badgeview_target);  // 右上角数字标记

        BadgeView badgeView = new BadgeView(mContext);
        badgeView.setTargetView(target);
        badgeView.setBadgeMargin(0, 6, 10, 0);
        badgeView.setTextSize(10);
        badgeView.setText(formatBadgeNumber(mBadgeCountList.get(position)));

        return view;
    }

    /**将int转化为String*/
    public static String formatBadgeNumber(int value) {
        if (value <= 0) {
            return null;
        }

        if (value < 100) {
            // equivalent to String#valueOf(int);
            return Integer.toString(value);
        }

        // my own policy
        return "99+";
    }
}

