package com.padedatingapp.ui.call;

import android.os.Bundle;

import com.padedatingapp.R;
import com.padedatingapp.base.BaseActivity;

import org.jetbrains.annotations.Nullable;

public class AudioCallActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.dialog_call;
    }


}
