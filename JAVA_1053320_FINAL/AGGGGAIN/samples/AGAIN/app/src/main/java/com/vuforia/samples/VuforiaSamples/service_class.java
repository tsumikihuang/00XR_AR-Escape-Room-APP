package com.vuforia.samples.VuforiaSamples;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.Tango.OnTangoUpdateListener;
import com.google.atap.tangoservice.TangoConfig;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoPoseData;

//**************************************

import android.util.Log;
import android.widget.Toast;

import com.google.atap.tangoservice.Tango;
import com.google.atap.tangoservice.TangoCoordinateFramePair;
import com.google.atap.tangoservice.TangoEvent;
import com.google.atap.tangoservice.TangoPoseData;
import com.google.atap.tangoservice.TangoXyzIjData;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class service_class extends Service {
    private static final String TAG = service_class.class.getSimpleName();
    private Tango mTango;
    private TangoConfig mConfig;

    @Override
    public IBinder onBind(Intent arg0) {            // 綁 定 服 務
// TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent)          // 解 除 綁 定 服 務
    {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate()                           // 建 立 服 務
    {
        super.onCreate();
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroy()                                                    // 銷 毀 服 務
    {
        super.onDestroy();
        Toast.makeText(this, "Service stop", Toast.LENGTH_SHORT).show();
        // TODO Auto-generated method stub
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)    // 啟 動 服 務
    {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Service start", Toast.LENGTH_SHORT).show();

        mTango = new Tango(service_class.this, new Runnable() {
            public void run() {
                synchronized (service_class.this) {

                    mConfig = setupTangoConfig(mTango);
                    mTango.connect(mConfig);
                    final ArrayList<TangoCoordinateFramePair> framePairs =
                            new ArrayList<TangoCoordinateFramePair>();
                    framePairs.add(new TangoCoordinateFramePair(
                            TangoPoseData.COORDINATE_FRAME_START_OF_SERVICE,
                            TangoPoseData.COORDINATE_FRAME_DEVICE));

                    mTango.connectListener(framePairs, new Tango.TangoUpdateCallback() {
                        @Override
                        public void onPoseAvailable(TangoPoseData pose) {
                            logPose(pose);
                        }
                        @Override
                        public void onXyzIjAvailable(TangoXyzIjData tangoXyzIjData) {
                        }
                        @Override
                        public void onFrameAvailable(int i) {
                        }
                    });
                }
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
    private void logPose(TangoPoseData pose) {
        StringBuilder stringBuilder = new StringBuilder();

        float translation[] = pose.getTranslationAsFloats();
        stringBuilder.append("Position: " +
                translation[0] + ", " + translation[1] + ", " + translation[2]);

        float orientation[] = pose.getRotationAsFloats();
        stringBuilder.append(". Orientation: " +
                orientation[0] + ", " + orientation[1] + ", " +
                orientation[2] + ", " + orientation[3]);

        Log.i(TAG, stringBuilder.toString());
    }
    private TangoConfig setupTangoConfig(Tango tango) {
        // Create a new Tango Configuration and enable the HelloMotionTrackingActivity API.
        TangoConfig config = tango.getConfig(TangoConfig.CONFIG_TYPE_DEFAULT);
        config.putBoolean(TangoConfig.KEY_BOOLEAN_MOTIONTRACKING, true);

        // Tango Service should automatically attempt to recover when it enters an invalid state.
        config.putBoolean(TangoConfig.KEY_BOOLEAN_AUTORECOVERY, true);
        return config;
    }

}