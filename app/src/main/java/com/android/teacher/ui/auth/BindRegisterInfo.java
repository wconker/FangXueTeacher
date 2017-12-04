package com.android.teacher.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.teacher.R;
import com.android.teacher.base.BaseActivity;
import com.android.teacher.base.CommonAdapter;
import com.android.teacher.base.CommonViewHolder;
import com.android.teacher.callback.MessageCallBack;
import com.android.teacher.entity.SchoolBean;
import com.android.teacher.newwork.MessageCenter;
import com.android.teacher.ui.Center.ActivityCenter;
import com.android.teacher.ui.Info.CreateSchool;
import com.android.teacher.ui.SecondPage.SendHomeWork;
import com.android.teacher.utils.JSONUtils;
import com.android.teacher.utils.SharedPrefsUtil;
import com.android.teacher.utils.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class BindRegisterInfo extends BaseActivity implements MessageCallBack {


    @Bind(R.id.schoolList)
    Spinner schoolList;
    @Bind(R.id.rightBtn)
    TextView rightBtn;
    @Bind(R.id.submit)
    Button submit;
    @Bind(R.id.back_btn)
    ImageView back;
    private MessageCenter messageCenter;
    private Observer<String> observer;
    @Bind(R.id.ClassName_et)
    EditText ClassName_et;
    String[] mItems;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<>();
    SchoolBean schoolBean;
    @Bind(R.id.gradeSpinner)
    Spinner gradeSpinner;
    int SCHOOLID = 0, GRADE = 0;


    @Override
    public void setButterKnife() {

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageCenter.setCallBackInterFace(this);
        messageCenter.SendYouMessage(messageCenter.ChooseCommand().School_getList());
    }

    @OnClick({R.id.submit, R.id.back_btn, R.id.rightBtn})
    void onClickMethod(View v) {
        switch (v.getId()) {
            case R.id.submit:
                int tid = Integer.valueOf(SharedPrefsUtil.getValue(this, "teacherXML", "teacherid", "0"));
                messageCenter.SendYouMessage(messageCenter.ChooseCommand().class_addInfo(SCHOOLID,
                        GRADE,
                        tid,
                        ClassName_et.getText().toString()));
                break;
            case R.id.rightBtn:
                startActivity(new Intent(BindRegisterInfo.this, CreateSchool.class));
                break;
            case R.id.back_btn:
                ThisFinis();
                break;
        }
    }

    @Override
    protected void setMyObserver() {
        super.setMyObserver();
        observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {


                JSONObject cmd = JSONUtils.StringToJSON(s);
                Gson gson = new Gson();
                switch (JSONUtils.getString(cmd, "cmd")) {
                    case "school.getList":
                        list.clear();
                        Type type = new TypeToken<SchoolBean>() {
                        }.getType();
                        schoolBean = gson.fromJson(s, type);
                        for (int i = 0; i < schoolBean.getData().size(); i++) {
                            list.add(schoolBean.getData().get(i).getSchoolname());
                        }
                        Log.e("BindRegisterInfo", schoolBean.getMessage());
                        adapter.notifyDataSetChanged();
                        break;
                    case "class.addInfo":

                        if (JSONUtils.getInt(cmd, "code", -1) == 1) {
                            finish();
                        }
                        Toast.FangXueToast(BindRegisterInfo.this, JSONUtils.getString(cmd, "message"));
                        break;
                }


            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_auth_bind_register_info;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        messageCenter = new MessageCenter();

        // 建立数据源
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setText("新增学校");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                GRADE = Integer.valueOf(BindRegisterInfo.this.getResources().getStringArray(R.array.grade)[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        schoolList.setAdapter(adapter);
        schoolList.setSelection(2, true);
        schoolList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                SCHOOLID = schoolBean.getData().get(pos).getSchoolid();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }


    @Override
    public void onMessage(String str) {
        DealMessageForMe(str, observer);
        Log.e("BindRegisterInfo", str);
    }


    class SpinerAdapter extends CommonAdapter<SchoolBean.DataBean> {

        public SpinerAdapter(Context context, List<SchoolBean.DataBean> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        public void setViewContent(CommonViewHolder viewHolder, SchoolBean.DataBean dataBean) {
            viewHolder.setText(R.id.gv_title, dataBean.getSchoolname());
        }
    }

}
