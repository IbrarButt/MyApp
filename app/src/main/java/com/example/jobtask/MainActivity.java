package com.example.jobtask;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {


    public static final String JOB_TAG="JOB_TAG";
    public FirebaseJobDispatcher firebaseJobDispatcher;

    Button b1,b2;
    private ImageView iv_image;
    private SurfaceView sv;

    private Bitmap bmp;


    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseJobDispatcher=new FirebaseJobDispatcher(new GooglePlayDriver(this));


        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job myJob = firebaseJobDispatcher.newJobBuilder()
                        .setService(MyJob.class)
                        // uniquely identifies the job
                        .setTag(JOB_TAG)
                        // one-off job
                        .setRecurring(true)
                        // don't persist past a device reboot
                        .setLifetime(Lifetime.FOREVER)
                        // start between 0 and 60 seconds from now
                        .setTrigger(Trigger.executionWindow(10, 15))
                        // don't overwrite an existing job with the same tag
                        .setReplaceCurrent(false)
                        // retry with exponential backoff
                        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                        // constraints that need to be satisfied for the job to run
                        .setConstraints(
                                // only run on an unmetered network
                                Constraint.ON_ANY_NETWORK

                        )
                        .build();

                firebaseJobDispatcher.mustSchedule(myJob);
                Toast.makeText(getApplicationContext(), "Service Start....", Toast.LENGTH_SHORT).show();

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseJobDispatcher.cancel(JOB_TAG);
                Toast.makeText(getApplicationContext(), "Service Stop", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //get camera parameters
        parameters = mCamera.getParameters();

        //set camera parameters
        mCamera.setParameters(parameters);
        mCamera.startPreview();

        //sets what code should be executed after the picture is taken
        Camera.PictureCallback mCall = new Camera.PictureCallback()
        {
            @Override
            public void onPictureTaken(byte[] data, Camera camera)
            {
                //decode the data obtained by the camera into a Bitmap
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //set the iv_image
                iv_image.setImageBitmap(bmp);
            }
        };

        mCamera.takePicture(null, null, mCall);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);

        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //stop the preview
        mCamera.stopPreview();
        //release the camera
        mCamera.release();
        //unbind the camera from this object
        mCamera = null;
    }
}
