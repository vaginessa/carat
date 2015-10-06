package edu.berkeley.cs.amplab.carat.android.fragments;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.berkeley.cs.amplab.carat.android.CaratApplication;
import edu.berkeley.cs.amplab.carat.android.R;
import edu.berkeley.cs.amplab.carat.android.activities.DashboardActivity;
import edu.berkeley.cs.amplab.carat.android.dialogs.BaseDialog;
import edu.berkeley.cs.amplab.carat.android.sampling.SamplingLibrary;
import edu.berkeley.cs.amplab.carat.android.ui.CircleDisplay;

/**
 * Created by Valto on 30.9.2015.
 */
public class DeviceFragment extends Fragment implements View.OnClickListener, Runnable {

    private DashboardActivity dashboardActivity;
    private RelativeLayout mainFrame;
    private CircleDisplay cd;

    private SurfaceHolder memoryUsedSurfaceHolder;
    private SurfaceHolder memoryActiveSurfaceHolder;
    private SurfaceHolder cpuUsageSurfaceHolder;
    private Thread drawingThread;

    private SurfaceView memoryUsedSurface;
    private SurfaceView memoryActiveSurface;
    private SurfaceView cpuUsageSurface;

    private Button memoryUsedButton;
    private Button memoryActiveButton;
    private Button cpuUsageButton;

    private TextView deviceModel;
    private TextView osVersion;
    private TextView caratID;
    private TextView batteryLife;

    private boolean locker = true;
    private float memoryUsedConverted = 0;
    private float memoryActiveConverted = 0;
    private float cpuUsageConverted = 0;
    private long[] lastPoint = null;
    private BaseDialog dialog;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.dashboardActivity = (DashboardActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainFrame = (RelativeLayout) inflater.inflate(R.layout.fragment_device, container, false);
        return mainFrame;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        initViewRefs();
        generateJScoreCircle();
        initListeners();
        setValues();
    }

    private void initViewRefs() {
        cd = (CircleDisplay) mainFrame.findViewById(R.id.jscore_progress_circle);
        memoryUsedSurface = (SurfaceView) mainFrame.findViewById(R.id.memory_used_surface);
        memoryActiveSurface = (SurfaceView) mainFrame.findViewById(R.id.memory_active_surface);
        cpuUsageSurface = (SurfaceView) mainFrame.findViewById(R.id.cpu_usage_surface);
        memoryUsedSurfaceHolder = memoryUsedSurface.getHolder();
        memoryActiveSurfaceHolder = memoryActiveSurface.getHolder();
        cpuUsageSurfaceHolder = cpuUsageSurface.getHolder();

        caratID = (TextView) mainFrame.findViewById(R.id.carat_id_value);
        deviceModel = (TextView) mainFrame.findViewById(R.id.device_model_value);
        osVersion = (TextView) mainFrame.findViewById(R.id.os_version_value);

        memoryUsedButton = (Button) mainFrame.findViewById(R.id.memory_used_info_button);
        memoryActiveButton = (Button) mainFrame.findViewById(R.id.memory_active_button);
        cpuUsageButton = (Button) mainFrame.findViewById(R.id.cpu_usage_button);
        batteryLife = (TextView) mainFrame.findViewById(R.id.battery_value);

    }

    private void initListeners() {
        cd.setOnClickListener(this);
        memoryUsedButton.setOnClickListener(this);
        memoryActiveButton.setOnClickListener(this);
        cpuUsageButton.setOnClickListener(this);
    }


    private void generateJScoreCircle() {
        cd.setValueWidthPercent(10f);
        cd.setTextSize(40f);
        cd.setColor(Color.argb(255, 247, 167, 27));
        cd.setDrawText(true);
        cd.setDrawInnerCircle(true);
        cd.setFormatDigits(0);
        cd.setTouchEnabled(false);
        cd.setUnit("");
        cd.setStepSize(1f);
    }

    private void setValues() {
        if (dashboardActivity.getJScore() == -1 || dashboardActivity.getJScore() == 0) {
            cd.setCustomText(new String[]{"N/A"});
        } else {
            cd.showValue((float) dashboardActivity.getJScore(), 99f, false);
        }

        osVersion.setText(SamplingLibrary.getOsVersion());
        deviceModel.setText(SamplingLibrary.getModel());
        caratID.setText(CaratApplication.myDeviceData.getCaratId());
        batteryLife.setText(dashboardActivity.getBatteryLife());

        setMemoryValues();

    }

    private void setMemoryValues() {
        int[] totalAndUsed = SamplingLibrary.readMeminfo();
        memoryUsedConverted = 1 - ((float) totalAndUsed[0] / totalAndUsed[1]);
        if (totalAndUsed.length > 2) {
            memoryActiveConverted = (float) totalAndUsed[2] / (totalAndUsed[3] + totalAndUsed[2]);
        }

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                long[] currentPoint = SamplingLibrary.readUsagePoint();

                float cpu = 0;
                if (lastPoint == null) {
                    lastPoint = currentPoint;
                } else {
                    cpu = (float) SamplingLibrary.getUsage(lastPoint, currentPoint);
                }
                cpuUsageConverted = cpu;

            }
        });
        drawMemoryValues();

    }

    private void drawMemoryValues() {
        drawingThread = new Thread(this);
        drawingThread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jscore_progress_circle:
                dialog = new BaseDialog(getContext(),
                        getString(R.string.jscore_dialog_title),
                        getString(R.string.jscore_explanation));
                dialog.showDialog();
                break;
            case R.id.memory_used_info_button:
                dialog = new BaseDialog(getContext(),
                        getString(R.string.memory_used_title),
                        getString(R.string.memory_used_explanation));
                dialog.showDialog();
                break;
            case R.id.memory_active_button:
                dialog = new BaseDialog(getContext(),
                        getString(R.string.memory_active_title),
                        getString(R.string.memory_active_explanation));
                dialog.showDialog();
                break;
            case R.id.cpu_usage_button:
                dialog = new BaseDialog(getContext(),
                        getString(R.string.cpu_usage_title),
                        getString(R.string.cpu_usage_explanation));
                dialog.showDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        while (locker) {
            if (!memoryUsedSurfaceHolder.getSurface().isValid()) {
                continue;
            }
            Canvas c = memoryUsedSurfaceHolder.lockCanvas();
            draw(c, 0);
            memoryUsedSurfaceHolder.unlockCanvasAndPost(c);

            if (!memoryActiveSurfaceHolder.getSurface().isValid()) {
                continue;
            }
            Canvas c1 = memoryActiveSurfaceHolder.lockCanvas();
            draw(c1, 1);
            memoryActiveSurfaceHolder.unlockCanvasAndPost(c1);

            if (!cpuUsageSurfaceHolder.getSurface().isValid()) {
                continue;
            }
            Canvas c2 = cpuUsageSurfaceHolder.lockCanvas();
            draw(c2, 2);
            cpuUsageSurfaceHolder.unlockCanvasAndPost(c2);
            locker = false;
        }
    }

    private void draw(Canvas canvas, int which) {
        RectF r;
        switch (which) {
            case 0:
                r = new RectF(0, 0, memoryUsedConverted * canvas.getWidth(), canvas.getHeight());
                break;
            case 1:
                r = new RectF(0, 0, memoryActiveConverted * canvas.getWidth(), canvas.getHeight());
                break;
            case 2:
                r = new RectF(0, 0, 0.3f * canvas.getWidth(), canvas.getHeight());
                //r = new RectF(0, 0, cpuUsageConverted * canvas.getWidth(), canvas.getHeight());
                break;
            default:
                r = new RectF(0, 0, 0, 0);
                break;
        }
        Log.d("debug", "*** CPU: " + cpuUsageConverted);
        canvas.drawColor(Color.argb(255, 180, 180, 180));
        Paint paint = new Paint();
        paint.setARGB(255, 75, 200, 127);
        canvas.drawRect(r, paint);
    }
}
