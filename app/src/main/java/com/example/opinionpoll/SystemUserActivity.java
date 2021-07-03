package com.example.opinionpoll;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.opinionpoll.utils.MyWebService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.opinionpoll.utils.ViewPagerAdapter;
import com.example.opinionpoll.databinding.ActivitySystemUserBinding;

public class SystemUserActivity extends AppCompatActivity {

    private static final String TAG = SystemUserActivity.class.getSimpleName();
    private DownloadManager downloadManager;
    private long downloadID;
    private Uri downloadUri;

    private ActivitySystemUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySystemUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        downloadUri = Uri.parse(MyWebService.BASE_URL + MyWebService.DOWNLOAD_FEED);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
                    Snackbar.make(view, "Storage is either full or read-only.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (isStoragePermissionGranted()) {
                    downloadID = download(downloadUri);
                }
            }
        });
    }

    // Check if external storage is available to at least read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "external storage Readable!");
            return true;
        }
        return false;
    }

    // Check if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d(TAG, "external storage Writable!" );
            return true;
        }
        return false;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
                return true;
            } else {
                Log.d(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { // permission is automatically granted on sdk<23 upon installation
            Log.d(TAG, "Permission is granted");
            return true;
        }
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Fetch the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            // Check if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            downloadID = download(downloadUri);
        }
    }

    private long download(Uri uri) {
        long downloadReference;
        String fileName = "report.xlsx";

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setTitle(fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription("Opinion Poll downloading in progress...");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setVisibleInDownloadsUi(true);

        //Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }
}