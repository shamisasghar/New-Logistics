package com.hypernymbiz.logistics.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.race604.drawable.wave.WaveDrawable;

/**
 * Created by Metis on 09-Feb-18.
 */

public class LoadingDialog extends Dialog {
    private String mMessage;
    private boolean isDisplayIcon;
    private static final int WAVE_SPEED = 4;
    private static final int WAVE_LENGTH = 40;
    private static final int WAVE_AMPLITUDE = 3;

    public LoadingDialog(Context context, String message) {
        super(context);
        mMessage = message;
    }

    public LoadingDialog(Context context, String message, boolean isDisplayIcon) {
        super(context);
        mMessage = message;
        this.isDisplayIcon = isDisplayIcon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        TextView messageText = (TextView) findViewById(R.id.text_message);
        WaveDrawable mWaveDrawable = new WaveDrawable(ContextCompat.getDrawable(getContext(),R.drawable.oil));
        ImageView imageView = (ImageView) findViewById(R.id.image_oil);
        mWaveDrawable.setWaveSpeed(AppUtils.dpToPx(WAVE_SPEED));
        mWaveDrawable.setWaveLength(AppUtils.dpToPx(WAVE_LENGTH));
        mWaveDrawable.setWaveAmplitude(AppUtils.dpToPx(WAVE_AMPLITUDE));
        mWaveDrawable.setIndeterminate(true);
        imageView.setImageDrawable(mWaveDrawable);
        if (isDisplayIcon) {
            messageText.setText(mMessage);
            imageView.setVisibility(View.GONE);
            ImageView iconImage = (ImageView) findViewById(R.id.icon_image);
            iconImage.setVisibility(View.VISIBLE);
        } else {
            messageText.setText(mMessage+"    ");
            imageView.setVisibility(View.VISIBLE);
        }
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
