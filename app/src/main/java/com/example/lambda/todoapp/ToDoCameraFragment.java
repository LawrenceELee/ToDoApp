package com.example.lambda.todoapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Lawrence on 3/30/2016.
 *
 * This is the controller for camera functionality.
 *
 */
public class ToDoCameraFragment extends Fragment{
    private static final String TAG = "ToDoCameraFragment";

    public static final String EXTRA_PHOTO_FILENAME = "com.example.lambda.todoapp.photo_filename";

    private Camera mCamera;             // ref to camera model/object.
    private SurfaceView mSurfaceView;   // ref to the what the "view finder" is looking at
    private View mProgressContainer;    // ref to the progress bar/circle

    // implementing callbacks object for takePicture(...)
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            // display the progress indicator
            mProgressContainer.setVisibility(View.VISIBLE);
        }
    };
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // create a filename
            String filename = UUID.randomUUID().toString() + ".jpg";
            // save the jpeg to disk;
            FileOutputStream fos = null;
            boolean success = true;

            try{
                fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(data);
            } catch (Exception e) {
                Log.e(TAG, "Error writing to file " + filename, e);
                success = false;
            } finally {
                try{
                    if( fos != null )   fos.close();
                } catch (Exception e){
                    Log.e(TAG, "Error closing file " + filename, e);
                    success = false;
                }
            }

            // set the photo filename on the result intent
            if( success ){
                Intent intent = new Intent();
                intent.putExtra(EXTRA_PHOTO_FILENAME, filename);
                getActivity().setResult(Activity.RESULT_OK, intent);
            } else {
                getActivity().setResult(Activity.RESULT_CANCELED);
            }

            getActivity().finish();
        }
    };


    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_camera, container, false);

        Button takePictureButton = (Button) view.findViewById(R.id.todo_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mCamera != null ) mCamera.takePicture(mShutterCallback, null, mJpegCallback);
            }
        });

        // set the progress bar to be invisible initially
        mProgressContainer = view.findViewById(R.id.todo_camera_progressContainer);
        mProgressContainer.setVisibility(View.INVISIBLE);

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
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
                parameters.setPictureSize(s.width, s.height);
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
