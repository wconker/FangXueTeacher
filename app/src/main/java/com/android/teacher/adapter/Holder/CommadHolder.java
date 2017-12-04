package com.android.teacher.adapter.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.teacher.R;
import com.android.teacher.base.BaseHolder;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.w3c.dom.Text;

/**
 * Created by softsea on 17/11/16.
 */

public class CommadHolder extends BaseHolder<String> {


    private final View item;

    public CommadHolder(View itemView) {
        super(itemView);
        this.item = itemView;
    }

    @Override
    public void getData(String d) {

        TextView t = item.findViewById(R.id.control2);
        t.setText(d);
    }
}
