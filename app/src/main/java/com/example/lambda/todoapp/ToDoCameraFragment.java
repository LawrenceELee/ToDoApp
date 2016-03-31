package com.example.lambda.todoapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by Lawrence on 3/30/2016.
 *
 * This is the controller for camera functionality.
 *
 */
public class ToDoCameraFragment extends Fragment{
    private static final String TAG = "ToDoCameraFragment";

    private Camera mCamera;             // ref to camera model/object.
    private SurfaceView mSurfaceView;   // ref to the what the "view finder" is looking at

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_camera, container, false);

        Button takePictureButton = (Button) view.findViewById(R.id.todo_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // temporarily tell the button to finish current activity and return to the previous one.
                getActivity().finish();
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.todo_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        // setType() and SURFACE_TYPE_PUSH_BUFFERS are both deprecated,
        // but are required for Camera preview to work on pre-3.0 devices.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                // tell camera to use this surface as its preview area
                try{
                    if( mCamera != null ){
                        mCamera.setPreviewDisplay(surfaceHolder);
                    }
                } catch (IOException ioe){
                    Log.e(TAG, "Error setting up preview display", ioe);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                // can no longer display on this surface, so stop the preview
                if( mCamera != null ){
                    mCamera.stopPreview();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
                if( mCamera == null )       return;

                // surface has changed size; update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(s.width, s.height);
                mCamera.setParameters(parameters);

                try{
                    mCamera.startPreview();
                } catch (Exception e){
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }
        });

        return view;
    }

    // simple alg to get the largest size available
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for( Camera.Size s : sizes){
            int area = s.width * s.height;
            if( area > largestArea ){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    // open() acquires the camera resource.
    @TargetApi(9)
    @Override
    public void onResume() {
        super.onResume();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            mCamera = Camera.open(0);
        } else {
            mCamera = Camera.open();
        }
    }

    // release the camera resource when not in the foreground since only 1 app can access camera at one time.
    @Override
    public void onPause() {
        super.onPause();

        if( mCamera != null ){
            mCamera.release();
            mCamera = null;
        }
    }
}
