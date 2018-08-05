package com.example.kevin.hyperbilirubinemia;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class DetectionActivity extends AppCompatActivity {

    private static int CAMERA_REQUEST_CODE = 1001;

    private CameraDevice camera;
    private String cameraID;
    private TextureView textureView;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private Size previewSize;
    private CameraManager cameraManager;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSession;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private CameraDevice.StateCallback stateCallback;
    private File appFolder;
    private String filePath;
    private static String NOTIFICATION_CHANNEL_ID = "EndNotif";
    private static int NOTIFICATION_ID = 3;

    private int x;

    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.DEBUGTAG, "called onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        surfaceTextureListener = surfaceTextureListener();
        stateCallback = stateCallback();

        addFabClickListener();
        x=0;
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener() {
        return new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                setupCamera();
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };
    }

    //camera callback
    private CameraDevice.StateCallback stateCallback() {
        return new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                DetectionActivity.this.camera = camera;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                camera.close();
                DetectionActivity.this.camera = null;
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                camera.close();
                DetectionActivity.this.camera = null;
            }
        };
    }


    private void setupCamera() {
        try {
            for (String cameraID : cameraManager.getCameraIdList()) {
                //Getting each camera characteristics
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);

                //CHoosing the back camera
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    //Geting the camera size for texture view
                    //get the camera characteristics key
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.cameraID = cameraID;
                }
            }
        } catch (CameraAccessException e) {
            Log.d(MainActivity.DEBUGTAG, "setupCamera exception");
        }
    }

    private void openCamera() {
        try {
            //Checking whether the app has the permission of accessing camera
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                /* stateCallBack is called when the camera is opened
                 * backgroundHandler is a handler that handle the callback
                 * null to use the current thread's looper
                 */
                cameraManager.openCamera(cameraID, stateCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.d(MainActivity.DEBUGTAG, "openCamera exception");
        }
    }

    //Create new thread and handler
    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_thread");
        backgroundThread.start();
        //Pass the thread looper
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void createPreviewSession() {
        try {
            textureView = findViewById(R.id.texture_view);
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            //Creating surface from surfaceTexture
            Surface surface = new Surface(surfaceTexture);

            captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            /* Create capture session
             * use singleton list for list parameter
             */
            camera.createCaptureSession(Collections.singletonList(surface), captureSessionsCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            Log.d(MainActivity.DEBUGTAG, "createpreviewsession exception");
        }

    }

    //Capture request state callback
    private CameraCaptureSession.StateCallback captureSessionsCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            if (camera == null) {
                return;
            }
            try {
                CaptureRequest captureRequest = captureRequestBuilder.build();

                DetectionActivity.this.cameraCaptureSession = session;
                DetectionActivity.this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, backgroundHandler);
            } catch (CameraAccessException e) {
                Log.d(MainActivity.DEBUGTAG, "capturesessioncalback exception");
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.d(MainActivity.DEBUGTAG, "Camera capture session cannot be configured");
        }
    };


    private File createGallery() {

        Log.d(MainActivity.DEBUGTAG, "address: " + filePath);
        return appFolder;
    }

    private File createImageFile(File appFolder) throws IOException {
        File storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        appFolder = new File(storagePath, getResources().getString(R.string.app_name));

        //Checking if the directory is not exist
        if (!appFolder.exists()) {
            boolean dirCreated = appFolder.mkdirs();

            if (!dirCreated) {
                Log.d(MainActivity.DEBUGTAG, "Cannot create a directory");
            }
        }
        filePath = appFolder.getParent() + "/Hyperbilirubinemia";

        String timeStamp = new SimpleDateFormat("yyyMMdd-HHmmss", Locale.getDefault()).format(new Date());

        String imageFileName = "image_" + timeStamp + ".jpg";
        Log.d(MainActivity.DEBUGTAG, imageFileName);

        filePath = filePath + "/" + imageFileName ;
        Log.d(MainActivity.DEBUGTAG, "file adres: " + filePath);

        File file = new File(appFolder, imageFileName);
        return file;


    }


    private void addFabClickListener() {
        FloatingActionButton fab = findViewById(R.id.take_picture_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(createImageFile(createGallery()));

                    textureView.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);

                    Intent serviceIntent = new Intent(DetectionActivity.this, ImageProcessingService.class);
                    serviceIntent.putExtra("IMAGE_FILE_PATH", filePath);

                    startService(serviceIntent);
                    Intent mainIntent = new Intent(DetectionActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                    Log.d(MainActivity.DEBUGTAG, "adsfads");
                    //Will use broadcast and receiver for service showing notifs

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();

                            filePath = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        textureView = findViewById(R.id.texture_view);
        //Calling the method backgroundThread
        openBackgroundThread();

        if (textureView.isAvailable()) {

            setupCamera();

            openCamera();

        } else {
            Log.d(MainActivity.DEBUGTAG, "Hey");

            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }

        // addFabClickListener();
    }


    private void lock() {
        try {
            cameraCaptureSession.capture(captureRequestBuilder.build(), null, backgroundHandler);
            Toast.makeText(DetectionActivity.this, "Image captured", Toast.LENGTH_LONG).show();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroundThread();
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
        if (camera != null) {
            camera.close();
            camera = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }
}
