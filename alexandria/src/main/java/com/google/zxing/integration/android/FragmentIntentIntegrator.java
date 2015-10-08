package com.google.zxing.integration.android;

/**
 * Created by ebal on 08/10/15.
 */
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.google.zxing.integration.android.IntentIntegrator;

public final class FragmentIntentIntegrator extends IntentIntegrator {

    private final Fragment fragment;

    public FragmentIntentIntegrator(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int code) {
        fragment.startActivityForResult(intent, code);
    }
}