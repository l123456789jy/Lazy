package com.github.lazy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.github.lazylibrary.util.DateUtil;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int day = DateUtil.getDay(new Date());
    }
}
