package com.weidou.tools;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainAcitivty extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edQuery = (EditText) findViewById(R.id.ed_query);
        final TextView tvNowQuery = (TextView) findViewById(R.id.tv_now_query);
        Button btnQueryConfirm = findViewById(R.id.btn_query_confirm);
        btnQueryConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edQuery.getText() != null
                        && !TextUtils.isEmpty(edQuery.getText().toString())
                        && edQuery.getText().toString().contains("%")) {
                    FileUtils.saveContentToSdcard("baifenbi", edQuery.getText().toString());
                    tvNowQuery.setText("当前比例" + FileUtils.getFileFromSdcard("baifenbi"));

                    Toast.makeText(MainAcitivty.this,"操作成功",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainAcitivty.this,"请输入正确比率",Toast.LENGTH_LONG).show();
                }
            }
        });
        tvNowQuery.setText("当前比例" + FileUtils.getFileFromSdcard("baifenbi"));
    }
}
