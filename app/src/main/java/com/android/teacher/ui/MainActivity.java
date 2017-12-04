package com.android.teacher.ui;

import android.content.Intent;
import android.os.Bundle;

import com.android.teacher.base.BaseActivity;
import com.android.teacher.R;
import com.android.teacher.callback.MessageCallBack;
import com.android.teacher.newwork.MessageCenter;
import com.android.teacher.ui.Center.ActivityCenter;
import com.android.teacher.ui.auth.GetPhoneForRegister;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements MessageCallBack {


    private MessageCenter messageCenter;

    @Override
    public void setButterKnife() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
//        messageCenter = new MessageCenter();
//        messageCenter.setCallBackInterFace(this);
//        messageCenter.SendYouMessage(messageCenter.ChooseCommand().School_getList());
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                        startActivity(new Intent(MainActivity.this, GetPhoneForRegister.class));
                        ThisFinis();

                    }
                });

    }

    @Override
    public void onMessage(String str) {

    }
}
