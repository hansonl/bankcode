package com.weidou.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.liqi.nohttputils.RxNoHttpUtils;
import com.liqi.nohttputils.interfa.OnIsRequestListener;
import com.liqi.nohttputils.nohttp.NoHttpInit;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class MainAcitivty extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText edQuery = (EditText) findViewById(R.id.ed_query);
        final TextView tvNowQuery = (TextView) findViewById(R.id.tv_now_query);
        Button btnQueryConfirm = findViewById(R.id.btn_query_confirm);
        final EditText edYanchi = (EditText) findViewById(R.id.ed_yanchi);
        final TextView tvNowYanchi = (TextView) findViewById(R.id.tv_now_yanchi);
        Button btnYanchiConfirm = findViewById(R.id.btn_yanchi_confirm);
        btnQueryConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edQuery.getText() != null
                        && !TextUtils.isEmpty(edQuery.getText().toString())) {
                    FileUtils.saveContentToSdcard("baifenbi", edQuery.getText().toString());
                    tvNowQuery.setText("当前比例" + FileUtils.getFileFromSdcard("baifenbi"));

                    Toast.makeText(MainAcitivty.this, "操作成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainAcitivty.this, "请输入正确比率", Toast.LENGTH_LONG).show();
                }
            }
        });
        tvNowQuery.setText("当前比例" + FileUtils.getFileFromSdcard("baifenbi"));

        tvNowYanchi.setText("当前延迟" + FileUtils.getFileFromSdcard("yanchi"));
        btnYanchiConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edYanchi.getText() != null
                        && !TextUtils.isEmpty(edYanchi.getText().toString())) {
                    FileUtils.saveContentToSdcard("yanchi", edYanchi.getText().toString());
                    tvNowYanchi.setText("当前延迟" + FileUtils.getFileFromSdcard("yanchi"));

                    Toast.makeText(MainAcitivty.this, "操作成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainAcitivty.this, "请输入正确延迟", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
