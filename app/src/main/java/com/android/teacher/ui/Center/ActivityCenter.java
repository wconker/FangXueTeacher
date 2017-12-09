package com.android.teacher.ui.Center;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.android.teacher.R;
import com.android.teacher.base.BaseActivity;
import com.android.teacher.entity.tabs.TabEntity;
import com.android.teacher.utils.ACache;
import com.android.teacher.utils.UpdateManager;
import com.android.teacher.utils.ViewFindUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;


public class ActivityCenter extends BaseActivity {
    private View mDecorView;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String[] mTitles = {"首页", "信息", "学生", "设置"};
    private int[] iconShow = {R.mipmap.lesson_blue, R.mipmap.message_blue, R.mipmap.student_blue, R.mipmap.set_blue};
    private int[] iconHide = {R.mipmap.lesson_gray, R.mipmap.message_gray, R.mipmap.student_gray, R.mipmap.set_gray};
    protected ACache aCache;
    protected int CurrentPos = 0;
    public CommonTabLayout mTabLayout;

    @Override
    public void setButterKnife() {

    }

    void CheckoutVersion() {
        UpdateManager manager = new UpdateManager(this);
        manager.checkUpdateInfo();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ActivityCenter", "CurrentPos" + CurrentPos);
        if (CurrentPos == 3)
            ((mineFragment) mFragments.get(3)).setCallBackInterFace();
        if (CurrentPos == 1)
            ((MessageFragment) mFragments.get(1)).setCallBackInterFace();
        if (CurrentPos == 0)
            ((indexFragment) mFragments.get(0)).indexFragmentCallBackSet();
        if (CurrentPos == 2)
            ((student) mFragments.get(2)).doRefrsh();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_center;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        aCache = ACache.get(this);
        setFragmentList();
        //检测版本更新
        CheckoutVersion();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], iconShow[i], iconHide[i]));
        }
        mDecorView = getWindow().getDecorView();
        mTabLayout = ViewFindUtils.find(mDecorView, R.id.tl);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mFragments.get(position).onResume();
                //页面返回的时候记录当前位置
                CurrentPos = position;

                switch (position) {
                    case 0:
                        ((indexFragment) mFragments.get(position)).onResume();
                        break;
                    case 1:
                        ((MessageFragment) mFragments.get(position)).setCallBackInterFace();
                        break;
                    case 2:
                        ((student) mFragments.get(position)).doRefrsh();
                        break;
                    case 3:
                        ((mineFragment) mFragments.get(position)).onResume();
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {


            }
        });

        mTabLayout.setTabData(mTabEntities, this, R.id.container, mFragments);
    }

    private void setFragmentList() {
        mFragments.add(indexFragment.newInstance());
        mFragments.add(MessageFragment.newInstance());
        mFragments.add(student.newInstance());
        mFragments.add(mineFragment.newInstance());
    }
}
