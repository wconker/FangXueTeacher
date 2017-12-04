package com.android.teacher.ui.Info;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.teacher.R;
import com.android.teacher.callback.MessageCallBack;
import com.android.teacher.newwork.MessageCenter;
import com.android.teacher.utils.JSONUtils;
import com.android.teacher.utils.Toast;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class AddSchedul extends Activity implements MessageCallBack {
    @Bind(R.id.back_btn)
    ImageView backBtn;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.dayName_et)
    EditText dayNameEt;
    @Bind(R.id.coursename_et)
    EditText coursenameEt;
    @Bind(R.id.fromtime_et)
    EditText fromtimeEt;
    @Bind(R.id.to_et)
    EditText toEt;
    @Bind(R.id.section_et)
    EditText sectionEt;
    @Bind(R.id.submit)
    Button submit;
    private int teacherid = 0;
    private int witch = 0;
    private MessageCenter messageCenter;
    private TimePickerDialog.OnTimeSetListener mdateListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            StringBuilder b = new StringBuilder();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                b.append(String.valueOf(timePicker.getHour()).length()==2?timePicker.getHour():"0"+timePicker.getHour()).append(":").append(String.valueOf(timePicker.getMinute()).length()==2?timePicker.getMinute():"0"+timePicker.getMinute());
            } else {
                b.append(timePicker.getCurrentHour().toString().length()==2?timePicker.getCurrentHour().toString():"0"+timePicker.getCurrentHour().toString()).
                        append(":").
                        append(timePicker.getCurrentMinute().toString().length()==2?timePicker.getCurrentMinute().toString():"0"+timePicker.getCurrentMinute().toString());
            }
            Log.d("课程表时间", b + " ");
            if (witch == 1) {
                fromtimeEt.setText(b);
            } else {
                toEt.setText(b);
            }
        }
    };

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

            if (JSONUtils.getString(cmd, "cmd").equals("schedule.addInfo")) {
                if (JSONUtils.getInt(cmd, "code", 0) == 1) {
                    finish();
                } else {
                    Toast.FangXueToast(AddSchedul.this, JSONUtils.getString(cmd, "message"));
                }
            }
            if (JSONUtils.getString(cmd, "cmd").equals("schedule.updateInfo")) {
                if (JSONUtils.getInt(cmd, "code", 0) == 1) {
                    finish();
                } else {
                    Toast.FangXueToast(AddSchedul.this, JSONUtils.getString(cmd, "message"));
                }
                Toast.FangXueToast(AddSchedul.this, JSONUtils.getString(cmd, "message"));
            }
            if (JSONUtils.getString(cmd, "cmd").equals("schedule.getinfo")) {
                if (JSONUtils.getInt(cmd, "code", 0) == 1) {
                    JSONObject object = JSONUtils.getSingleJSON(cmd, "data", 0);
                    dayNameEt.setText(JSONUtils.getString(object, "daynumber"));
                    coursenameEt.setText(JSONUtils.getString(object, "coursename"));
                    fromtimeEt.setText(JSONUtils.getString(object, "fromtime"));
                    toEt.setText(JSONUtils.getString(object, "totime"));
                    sectionEt.setText(JSONUtils.getString(object, "sectionname"));
                } else {
                    Toast.FangXueToast(AddSchedul.this, JSONUtils.getString(cmd, "message"));
                }
            }
        }
    };


    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_info_activity_add_schedul);
        id = getIntent().getIntExtra("id", 0);


        ButterKnife.bind(this);
        toEt.setFocusable(false);
        fromtimeEt.setFocusable(false);
        toEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                witch = 2;
                TimePickerDialog d = new TimePickerDialog(AddSchedul.this, mdateListener, 12, 12, true);
                d.show();
            }
        });
        fromtimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                witch = 1;
                TimePickerDialog d = new TimePickerDialog(AddSchedul.this, mdateListener, 12, 12, true);
                d.show();
            }
        });
        teacherid = getIntent().getIntExtra("teacherid", 0);
        messageCenter = new MessageCenter();
        messageCenter.setCallBackInterFace(this);
        initView();

        if (id > 0) {
            messageCenter.SendYouMessage(messageCenter.ChooseCommand().schedule_getInfo(id));
        }
    }

    private void initView() {
        toolbarTitle.setText("添加课程表");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id <= 0) {
                    messageCenter.SendYouMessage(messageCenter.ChooseCommand().schedule_addInfo(
                            teacherid,
                            dayNameEt.getText().toString(),
                            coursenameEt.getText().toString(),
                            fromtimeEt.getText().toString(),
                            toEt.getText().toString(),
                            sectionEt.getText().toString()
                    ));
                } else {
                    messageCenter.SendYouMessage(messageCenter.ChooseCommand().schedule_updateInfo(
                            id,
                            dayNameEt.getText().toString(),
                            coursenameEt.getText().toString(),
                            fromtimeEt.getText().toString(),
                            toEt.getText().toString(),
                            sectionEt.getText().toString()
                    ));
                }
            }
        });
    }

    @Override
    public void onMessage(String str) {

        Log.e("课程表添加", str);
        Observable.just(str)
                .observeOn(AndroidSchedulers
                        .mainThread())
                .subscribe(observer);
    }
}
