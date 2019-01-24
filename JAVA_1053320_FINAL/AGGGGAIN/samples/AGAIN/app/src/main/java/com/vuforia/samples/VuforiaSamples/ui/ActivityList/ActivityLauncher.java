/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/


package com.vuforia.samples.VuforiaSamples.ui.ActivityList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.vuforia.samples.VuforiaSamples.R;
//**************************************************************************************************************** A D D
import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.Tango.OnTangoUpdateListener;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoErrorException;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoInvalidException;
import com.google.atap.tangoservice.TangoOutOfDateException;
import com.google.atap.tangoservice.TangoPointCloudData;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;
import com.vuforia.samples.VuforiaSamples.R;

import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
//*****************************************************************************************************************

// This activity starts activities which demonstrate the Vuforia features
public class ActivityLauncher extends ListActivity implements NumberPicker.OnValueChangeListener,NumberPicker.OnScrollListener,NumberPicker.Formatter
{
    private Button btn,misson1,location_get,finish;                            //******************************************************* A D D
    private TextView location_textview,misson_list,misson_list2,textView4;
    static float[] translation,orientation;
    private boolean misson1_bool=false,misson2_bool=false;
    private NumberPicker NumberPicker1,NumberPicker2,NumberPicker3,NumberPicker4;

    private String[] mActivities = {"神奇透鏡"};
    //******************************************************************************************************************** A D D
    private static final String TAG = ActivityLauncher.class.getSimpleName();
    private Tango mTango;
    private TangoConfig mConfig;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activities_list_text_view, mActivities);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activities_list);
        setListAdapter(adapter);

        //********************************************************************************************************** A D D
        //指派btn來取得視角(視窗)的Button按鈕
        btn = (Button)findViewById(R.id.btn);
        misson1 = (Button)findViewById(R.id.misson1);
        location_get= (Button)findViewById(R.id.location_get);
        finish= (Button)findViewById(R.id.finish);
        //註冊按鈕事件
        btn.setOnClickListener(listener);
        misson1.setOnClickListener(listener);
        location_get.setOnClickListener(listener);
        finish.setOnClickListener(listener);

        location_textview = (TextView) findViewById(R.id.location_textview);
        misson_list2 = (TextView) findViewById(R.id.misson_list2);
        textView4= (TextView) findViewById(R.id.textView4);

        NumberPicker1=(NumberPicker) findViewById(R.id.numberPicker1);
        NumberPicker2=(NumberPicker) findViewById(R.id.numberPicker2);
        NumberPicker3=(NumberPicker) findViewById(R.id.numberPicker3);
        NumberPicker4=(NumberPicker) findViewById(R.id.numberPicker4);
        init();

        new AlertDialog.Builder(ActivityLauncher.this)
                .setTitle("故事摘要")//設定視窗標題
                .setMessage("恩...這裡是電腦教室? \n糟了，看來我上課又睡著了\n" +
                        "怎麼教室裡一個人影也沒有? \n不會是已經下課很久了吧，趕快離開教室吧\n" +
                        "(走到門口，準備開門...)\n" +
                        "喀..喀...\n" +
                        "咦?門怎麼被鎖住了? \n難道我被困在這間教室了?! \n" +
                        "..................必須要想辦法離開才行\n" +
                        "打個電話給同學求救好了\n...疑? 手機沒訊號? \n雖然偶爾會收不到訊號...偏偏在這個時候? \n真倒楣\n" +
                        "先回我的座位，用教室的電腦向外求救吧")//設定顯示的文字
                .show();//呈現對話視窗
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {

        Intent intent = new Intent(this, AboutScreen.class);
        intent.putExtra("ABOUT_TEXT_TITLE", mActivities[position]);

        /*switch (position)
        {
            case 0:*/
        if(misson1_bool==true) {
            intent.putExtra("ACTIVITY_TO_LAUNCH",
                    "app.ImageTargets.ImageTargets");
            intent.putExtra("ABOUT_TEXT", "ImageTargets/IT_about.html");
                /*break;
        }*/
            startActivity(intent);
        }
    }

    //*************************************************************************************************************** A D D
    private Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn) {
                //產生視窗物件
                new AlertDialog.Builder(ActivityLauncher.this)
                        .setTitle("故事摘要")//設定視窗標題
                        .setMessage("恩...這裡是電腦教室? \n糟了，看來我上課又睡著了\n" +
                        "怎麼教室裡一個人影也沒有? \n不會是已經下課很久了吧，趕快離開教室吧\n" +
                        "(走到門口，準備開門...)\n" +
                        "喀..喀...\n" +
                        "咦?門怎麼被鎖住了? \n難道我被困在這間教室了?! \n" +
                        "..................必須要想辦法離開才行\n" +
                        "打個電話給同學求救好了\n...疑? 手機沒訊號? \n雖然偶爾會收不到訊號...偏偏在這個時候? \n真倒楣\n" +
                        "先回我的座位，用教室的電腦向外求救吧")//設定顯示的文字
                        .show();//呈現對話視窗
            }
            if (v.getId() == R.id.misson1) {
                if (translation[0] < -1.65 && translation[0] > -2.36 && translation[1] < 3.53 && translation[1] > 2.73) {      // && translation[2] < 0.49 && translation[2] > -0.01                                                                 //對的位置
                    if (orientation[0] < 0.51 && orientation[0] > 0.25 && orientation[1] < 0.6 && orientation[1] > 0.3 && orientation[2] < 0.61 && orientation[2] > 0.47 && orientation[3] < 0.71 && orientation[3] > 0.4) {                                                                   //對的方向
                //if (translation[0] >0.1) {                                                                       //對的位置
                    //if (orientation[0] >0) {                                                                   //對的方向
                        textView4.setText("任務二、");
                    misson_list2.setText("請找出UNO的 4張數字卡+2張功能卡+放卡片的盒子。所有東西都可以用\"神奇透鏡\"照照看");
                        misson1_bool=true;
                        new AlertDialog.Builder(ActivityLauncher.this)
                                .setTitle("訊息!")//設定視窗標題
                                .setMessage("呼~終於和同學連絡上了\n" +
                                        "甚麼?! \n竟然叫我找上次上課玩時弄丟的UNO卡牌?! \n有沒有搞錯?\n" +
                                        "說甚麼是離開教室的密碼?!\n 可惡阿...我被耍了嗎? 這群損友...\n")//設定顯示的文字
                                .show();//呈現對話視窗
                    } else {                                                                   //錯的方向
                        new AlertDialog.Builder(ActivityLauncher.this)
                                .setTitle("必須使用電腦")//設定視窗標題
                                .setMessage("用學校電腦聯繫外面的人看看，請將手機正面朝向電腦螢幕試試")//設定顯示的文字
                                .show();//呈現對話視窗
                    }
                } else if (translation[0] < -6.5 && translation[0] > -8.1 && translation[1] < 0.02 && translation[1] >-1.53) {                                                                       //對的位置
                    new AlertDialog.Builder(ActivityLauncher.this)
                            .setTitle("這裡是老師上課的位置")//設定視窗標題
                            .setMessage("從這裡看教室看得一清二楚呢\n希望上課玩牌沒有被發現...")//設定顯示的文字
                            .show();//呈現對話視窗
                }else if (translation[0] < 1.42 && translation[0] > -0.65 && translation[1] < -0.5 && translation[1] >-1.7) {                                                                       //對的位置
                    new AlertDialog.Builder(ActivityLauncher.this)
                            .setTitle("這是門口")//設定視窗標題
                            .setMessage("能趕快從這裏出去就好了...")//設定顯示的文字
                            .show();//呈現對話視窗
                } else
                    new AlertDialog.Builder(ActivityLauncher.this)
                            .setTitle("這並不是你的座位")//設定視窗標題
                            .setMessage("疑?我的座位好像不是這個...")//設定顯示的文字
                            .show();//呈現對話視窗
            }
            if (v.getId() == R.id.location_get) {
                location_textview.setText("位置資訊\nPosition: \n" +
                        translation[0] + ", " + translation[1] + ", " + translation[2] + "\nOrientation: \n" +
                        orientation[0] + ", " + orientation[1] + ", " +
                        orientation[2] + ", " + orientation[3]);
            }
            if (v.getId() == R.id.finish) {
                if(9==NumberPicker1.getValue() && 7==NumberPicker2.getValue() && 7==NumberPicker3.getValue() && 5==NumberPicker4.getValue())
                    new AlertDialog.Builder(ActivityLauncher.this)
                            .setTitle("恭喜破關~~~")//設定視窗標題
                            .setMessage("喀擦!\n!!!\n教室門開了!!!\n終於...")//設定顯示的文字
                            //設定結束的子視窗
                            .show();//呈現對話視窗
                else
                    new AlertDialog.Builder(ActivityLauncher.this)
                            .setTitle("破解失敗")//設定視窗標題
                            .setMessage("humm........打不開....密碼可能不是這個")//設定顯示的文字
                            //設定結束的子視窗
                            .show();//呈現對話視窗
            }
        }
    };
    /*****************************************************************************************************************Tango*/

    @Override
    protected void onResume() {
        super.onResume();

        // Initialize Tango Service as a normal Android Service. Since we call mTango.disconnect()
        // in onPause, this will unbind Tango Service, so every time onResume gets called we
        // should create a new Tango object.
        mTango = new Tango(ActivityLauncher.this, new Runnable() {
            // Pass in a Runnable to be called from UI thread when Tango is ready; this Runnable
            // will be running on a new thread.
            // When Tango is ready, we can call Tango functions safely here only when there are no
            // UI thread changes involved.
            @Override
            public void run() {
                synchronized (ActivityLauncher.this) {
                    try {
                        mConfig = setupTangoConfig(mTango);
                        mTango.connect(mConfig);
                        startupTango();
                    } catch (TangoOutOfDateException e) {
                        Log.e(TAG, getString(R.string.exception_out_of_date), e);
                        showsToastAndFinishOnUiThread(R.string.exception_out_of_date);
                    } catch (TangoErrorException e) {
                        Log.e(TAG, getString(R.string.exception_tango_error), e);
                        showsToastAndFinishOnUiThread(R.string.exception_tango_error);
                    } catch (TangoInvalidException e) {
                        Log.e(TAG, getString(R.string.exception_tango_invalid), e);
                        showsToastAndFinishOnUiThread(R.string.exception_tango_invalid);
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        synchronized (this) {
            try {
                mTango.disconnect();
            } catch (TangoErrorException e) {
                Log.e(TAG, getString(R.string.exception_tango_error), e);
            }
        }
    }

    /**
     * Sets up the tango configuration object. Make sure mTango object is initialized before
     * making this call.
     */

    private TangoConfig setupTangoConfig(Tango tango) {
        // Create a new Tango Configuration and enable the HelloMotionTrackingActivity API.
        TangoConfig config = tango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);

        // Tango Service should automatically attempt to recover when it enters an invalid state.
        config.putBoolean(TangoConfig.KEY_BOOLEAN_AUTORECOVERY, true);
        return config;
    }

    /**
     * Set up the callback listeners for the Tango Service and obtain other parameters required
     * after Tango connection.
     * Listen to new Pose data.
     */

    private void startupTango() {
        // Lock configuration and connect to Tango.
        // Select coordinate frame pair.
        final ArrayList<TangoCoordinateFramePair> framePairs =
                new ArrayList<TangoCoordinateFramePair>();
        framePairs.add(new TangoCoordinateFramePair(
                TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                TangoPoseData.COORDINATE_FRAME_DEVICE));

        // Listen for new Tango data.
        mTango.connectListener(framePairs, new OnTangoUpdateListener() {
            @Override
            public void onPoseAvailable(final TangoPoseData pose) {
                logPose(pose);
            }

            @Override
            public void onXyzIjAvailable(TangoXyzIjData xyzIj) {
                // We are not using onXyzIjAvailable for this app.
            }

            @Override
            public void onPointCloudAvailable(TangoPointCloudData pointCloud) {
                // We are not using onPointCloudAvailable for this app.
            }

            @Override
            public void onTangoEvent(final TangoEvent event) {
                // Ignoring TangoEvents.
            }

            @Override
            public void onFrameAvailable(int cameraId) {
                // We are not using onFrameAvailable for this application.
            }
        });
    }

    /**
     * Log the Position and Orientation of the given pose in the Logcat as information.
     *
     * @param pose the pose to log.
     */

    private void logPose(TangoPoseData pose) {
        StringBuilder stringBuilder = new StringBuilder();

        translation = pose.getTranslationAsFloats();
        stringBuilder.append("Position: " +
                translation[0] + ", " + translation[1] + ", " + translation[2]);

        orientation = pose.getRotationAsFloats();
        stringBuilder.append(". Orientation: " +
                orientation[0] + ", " + orientation[1] + ", " +
                orientation[2] + ", " + orientation[3]);

        Log.i(TAG, stringBuilder.toString());

    }

    /**
     * Display toast on UI thread.
     *
     * @param resId The resource id of the string resource to use. Can be formatted text.
     */

    private void showsToastAndFinishOnUiThread(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ActivityLauncher.this,
                        getString(resId), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    //************************************************************************************************************* P I C K E R
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {/*
        Toast.makeText(
                this,
                "原来的值 " + oldVal + "--新值: "
                        + newVal, Toast.LENGTH_SHORT).show();*/
    }

    public void onScrollStateChange(NumberPicker view, int scrollState) {

        /*switch (scrollState) {

            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                Toast.makeText(this, "后续滑动(飞呀飞，根本停下来)", Toast.LENGTH_LONG)
                        .show();
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                Toast.makeText(this, "不滑动", Toast.LENGTH_LONG).show();
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Toast.makeText(this, "滑动中", Toast.LENGTH_LONG)
                        .show();
                break;
        }*/
    }


    private void init() {
        NumberPicker1.setFormatter(this);
        NumberPicker1.setOnValueChangedListener(this);
        NumberPicker1.setOnScrollListener(this);
        NumberPicker1.setMaxValue(9);
        NumberPicker1.setMinValue(1);
        NumberPicker1.setValue(5);

        NumberPicker2.setFormatter(this);
        NumberPicker2.setOnValueChangedListener(this);
        NumberPicker2.setOnScrollListener(this);
        NumberPicker2.setMaxValue(9);
        NumberPicker2.setMinValue(1);
        NumberPicker2.setValue(5);

        NumberPicker3.setFormatter(this);
        NumberPicker3.setOnValueChangedListener(this);
        NumberPicker3.setOnScrollListener(this);
        NumberPicker3.setMaxValue(9);
        NumberPicker3.setMinValue(1);
        NumberPicker3.setValue(5);

        NumberPicker4.setFormatter(this);
        NumberPicker4.setOnValueChangedListener(this);
        NumberPicker4.setOnScrollListener(this);
        NumberPicker4.setMaxValue(9);
        NumberPicker4.setMinValue(1);
        NumberPicker4.setValue(5);
    }
}
