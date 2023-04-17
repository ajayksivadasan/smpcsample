package com.aks.smpc.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Entity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aks.smpc.R;
import com.aks.smpc.adapter.ListCapturedImagesAdapter;
import com.aks.smpc.data.TableData;
import com.aks.smpc.di.Table2DAO;
import com.aks.smpc.di.TableDAO;
import com.aks.smpc.utils.RoomDBase;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Inject
    TableDAO tableDAO;
    Context context;
    String imgStorageLocation;
    ImageCapture imageCapture;
    LinearLayout llPreview;
    ImageView btSignIn;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    PreviewView camPreviewView;
    ImageView imgPreview;
    LinearLayout llAdd;
    ImageView btAdd;
    RecyclerView rvFishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        camPreviewView = findViewById(R.id.camPreviewView);
        imgPreview = findViewById(R.id.imgPreview);
        llPreview = findViewById(R.id.llPreview);
        btSignIn = findViewById(R.id.btSignIn);
        llAdd = findViewById(R.id.llAdd);
        btAdd = findViewById(R.id.btAdd);
        rvFishList = findViewById(R.id.rvFishList);
        if (checkPermissions()) {
            requestPermissions();
        } else {
            requestPermissions();
        }
        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        rvFishList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        setAdapter();
        btSignIn.setOnClickListener(v -> {
            captureImage();
        });

        btAdd.setOnClickListener(v -> {
            TableData tableData = new TableData();
            tableData.setString1(imgStorageLocation);
            SimpleDateFormat sdfImage = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss:a", Locale.getDefault());
            tableData.setString2(sdfImage.format(Calendar.getInstance().getTimeInMillis()));
            tableDAO.insertData(tableData);
            Table2DAO table2DAO= RoomDBase.provideRoomDb(context).providesTable2DAO();
            llPreview.setVisibility(View.VISIBLE);
            imgPreview.setVisibility(View.GONE);
            btSignIn.setVisibility(View.VISIBLE);
            llAdd.setVisibility(View.GONE);
            btAdd.setVisibility(View.GONE);
            startCamera();
setAdapter();
        });
    }

    private void setAdapter() {
        ListCapturedImagesAdapter listCapturedImagesAdapter=new ListCapturedImagesAdapter(tableDAO.getAllTAbleDAta(), context);
        listCapturedImagesAdapter.setInterface(tableData ->{
            tableDAO.deleteData(tableData);
            setAdapter();
        });
        rvFishList.setAdapter(listCapturedImagesAdapter);
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION}, 44);
    }
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        camPreviewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
        ViewGroup.LayoutParams layoutParams = camPreviewView.getLayoutParams();
        camPreviewView.setLayoutParams(layoutParams);
        imgPreview.setLayoutParams(layoutParams);
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetRotation(camPreviewView.getDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(camPreviewView.getSurfaceProvider());
        cameraProvider.unbindAll();
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage() {
        SimpleDateFormat sdfImage = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss_a", Locale.getDefault());
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
        String id = sharedPreferences.getString("ID", "0");
        File file = new File(getExternalCacheDir() + "/file1_" + id + "_" + sdfImage.format(Calendar.getInstance().getTime()) + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        if (imageCapture != null) {
            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    // insert your code here.
                    imgStorageLocation = Objects.requireNonNull(outputFileResults.getSavedUri()).toString();
                    Log.e(TAG, "onImageSaved:2 " + imgStorageLocation);
                    Glide.with(context)
                            .load(outputFileResults.getSavedUri())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imgPreview);
                    llPreview.setVisibility(View.GONE);
                    imgPreview.setVisibility(View.VISIBLE);
                    btSignIn.setVisibility(View.GONE);
                    llAdd.setVisibility(View.GONE);
                    btAdd.setVisibility(View.VISIBLE);
                    try {
                        cameraProviderFuture.get().unbindAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@NonNull ImageCaptureException error) {
                    // insert your code here.
                    error.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        context=this;
        startCamera();
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(context));

    }
}