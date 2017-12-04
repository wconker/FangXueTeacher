package com.android.teacher.ui.Info;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.teacher.R;
import com.android.teacher.callback.MessageCallBack;
import com.android.teacher.newwork.MessageCenter;
import com.android.teacher.ui.auth.GetPhoneForRegister;
import com.android.teacher.utils.JSONUtils;
import com.android.teacher.utils.SharedPrefsUtil;
import com.android.teacher.utils.Toast;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class AddTeacher extends Activity implements MessageCallBack {
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.teacherName_et)
    EditText teacherName_et;
    @Bind(R.id.lesson_et)
    EditText lesson_et;
    @Bind(R.id.teacherMobile_et)
    EditText teacherMobile_et;
    @Bind(R.id.submit)
    Button submit;
    private int teacherid;
    private MessageCenter messageCenter;
    private Observer<String> observer = new Observer<String>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(String s) {
            JSONObject cmd = JSONUtils.StringToJSON(s);
            if (JSONUtils.getString(cmd, "cmd").equals("teacher.addInfo")) {
                if (JSONUtils.getInt(cmd, "code", 0) == 1) {
                    SharedPrefsUtil.putValue(AddTeacher.this, "teacherXML", "lesson", lesson_et.getText().toString()); //登錄手機
                    finish();
                    Toast.FangXueToast(AddTeacher.this, JSONUtils.getString(cmd, "message"));
                } else {
                    Toast.FangXueToast(AddTeacher.this, JSONUtils.getString(cmd, "message"));
                }
            }
            if (JSONUtils.getString(cmd, "cmd").equals("teacher.getinfo")) {
                if (JSONUtils.getInt(cmd, "code", 0) == 1) {
                    JSONObject object = JSONUtils.getSingleJSON(cmd, "data", 0);
                    teacherName_et.setText(JSONUtils.getString(object, "teachername"));
                    lesson_et.setText(JSONUtils.getString(object, "lesson"));
                    teacherMobile_et.setText(JSONUtils.getString(object, "mobile"));
                } else {
                    Toast.FangXueToast(AddTeacher.this, JSONUtils.getString(cmd, "message"));
                }

            }
            if (JSONUtils.getString(cmd, "cmd").equals("teacher.updateInfo")) {
                if (JSONUtils.getInt(cmd, "code", 0) == 1) {
                    SharedPrefsUtil.putValue(AddTeacher.this, "teacherXML", "lesson", lesson_et.getText().toString()); //登錄手機
                    finish();

                    Toast.FangXueToast(AddTeacher.this, JSONUtils.getString(cmd, "message"));
                } else {
                    Toast.FangXueToast(AddTeacher.this, JSONUtils.getString(cmd, "message"));
                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_center_activity_add_teacher);
        ButterKnife.bind(this);
        messageCenter = new MessageCenter();
        messageCenter.setCallBackInterFace(this);

        teacherid = getIntent().getIntExtra("teacherid", 0);
        toolbarTitle.setText("新增老师");
        if (teacherid > 0) {
            toolbarTitle.setText("修改老师信息");
            messageCenter.SendYouMessage(messageCenter.ChooseCommand().teacher_getinfo(teacherid));
            submit.setText("修改");
        } else {

            toolbarTitle.setText("新增老师");
            submit.setText("确定");
        }

        initView();
    }

    private void initView() {

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teacherid > 0) {
                    messageCenter.SendYouMessage(messageCenter.ChooseCommand().teacher_updateInfo(teacherid,
                            teacherName_et.getText().toString(),
                            teacherMobile_et.getText().toString(),
                            lesson_et.getText().toString()));
                } else {
                    messageCenter.SendYouMessage(messageCenter.ChooseCommand().teacher_addInfo(
                            "",
                            teacherName_et.getText().toString(),
                            teacherMobile_et.getText().toString(),
                            lesson_et.getText().toString()));
                }
            }
        });
    }

    @Override
    public void onMessage(String str) {

        Log.e("addTeacher", str);
        Observable.just(str)
                .observeOn(AndroidSchedulers
                        .mainThread())
                .subscribe(observer);

    }
}
