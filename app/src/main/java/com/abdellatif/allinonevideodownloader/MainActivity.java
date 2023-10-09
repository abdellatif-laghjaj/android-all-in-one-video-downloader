package com.abdellatif.allinonevideodownloader;

import static com.yausername.youtubedl_android.YoutubeDL.UpdateStatus.ALREADY_UP_TO_DATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yausername.youtubedl_android.YoutubeDL;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStreamingExample;
    private Button btnDownloadingExample;
    private Button btnCommandExample;
    private Button btnUpdate;

    //Image Buttons
    private ImageButton btnTheme1, btnTheme2, btnTheme3, btnTheme4, btnTheme5;
    private ProgressBar progressBar;
    private boolean updating = false;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "MainActivity";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hide the shadow of the action bar
        getSupportActionBar().setElevation(0);

        initViews();
        initListeners();
        btnUpdate.setVisibility(View.GONE);
        updateYoutubeDL();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void initListeners() {
        btnStreamingExample.setOnClickListener(this);
        btnDownloadingExample.setOnClickListener(this);
        //btnCommandExample.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void initViews() {
        btnStreamingExample = findViewById(R.id.btn_streaming_example);
        btnDownloadingExample = findViewById(R.id.btn_downloading_example);
        //btnCommandExample = findViewById(R.id.btn_command_example);
        btnUpdate = findViewById(R.id.btn_update);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_streaming_example: {
                Intent i = new Intent(MainActivity.this, StreamingExampleActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btn_downloading_example: {
                Intent i = new Intent(MainActivity.this, DownloadingExampleActivity.class);
                startActivity(i);
                break;
            }
//            case R.id.btn_command_example: {
//                Intent i = new Intent(MainActivity.this, CommandExampleActivity.class);
//                startActivity(i);
//                break;
//            }
            case R.id.btn_update: {
                updateYoutubeDL();
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu: {
                showMenuDialog();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateYoutubeDL() {
        if (updating) {
            //Toast.makeText(MainActivity.this, "update is already in progress", Toast.LENGTH_LONG).show();
            Toasty.info(MainActivity.this, "Update is already in progress", Toast.LENGTH_LONG, true).show();
            return;
        }

        updating = true;
        progressBar.setVisibility(View.VISIBLE);
        Disposable disposable = Observable.fromCallable(() -> YoutubeDL.getInstance().updateYoutubeDL(this, YoutubeDL.UpdateChannel._STABLE))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    progressBar.setVisibility(View.GONE);
                    switch (status) {
                        case DONE:
                            //Toast.makeText(MainActivity.this, "update successful", Toast.LENGTH_LONG).show();
                            Toasty.success(MainActivity.this, "Update successful", Toast.LENGTH_LONG, true).show();
                            break;
                        case ALREADY_UP_TO_DATE:
                            //Toast.makeText(MainActivity.this, "already up to date", Toast.LENGTH_LONG).show();
                            Toasty.info(MainActivity.this, "Already up to date", Toast.LENGTH_LONG, true).show();
                            break;
                        default:
                            //Toast.makeText(MainActivity.this, status.toString(), Toast.LENGTH_LONG).show();
                            Toasty.error(MainActivity.this, status.toString(), Toast.LENGTH_LONG, true).show();
                            break;
                    }
                    updating = false;
                }, e -> {
                    if (BuildConfig.DEBUG) Log.e(TAG, "failed to update", e);
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(MainActivity.this, "update failed", Toast.LENGTH_LONG).show();
                    Toasty.error(MainActivity.this, "Update failed", Toast.LENGTH_LONG, true).show();
                    updating = false;
                });
        compositeDisposable.add(disposable);
    }

    private void showAboutDialog() {
        //show dialog
        String about = getString(R.string.about_message);
        String version = "Version: " + BuildConfig.VERSION_NAME;
        String message = about + "\n\n" + version;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setMessage(message);
        builder.setPositiveButton("Close", null);
        builder.show();
    }

    private void showMenuDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout,
                (LinearLayout) findViewById(R.id.bottom_sheet_container));
        view.findViewById(R.id.about_container).setOnClickListener(v -> {
            showAboutDialog();
            dialog.dismiss();
        });
        view.findViewById(R.id.privacy_policy_container).setOnClickListener(v -> {
            //openn link in browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)));
            startActivity(browserIntent);
            dialog.dismiss();
        });
        view.findViewById(R.id.more_apps_container).setOnClickListener(v -> {
            //open google play store
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.more_apps_url)));
            startActivity(browserIntent);
            dialog.dismiss();
        });
        view.findViewById(R.id.update_container).setOnClickListener(v -> {
            updateYoutubeDL();
            dialog.dismiss();
        });
        view.findViewById(R.id.contact_container).setOnClickListener(v -> {
            //open email client
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.developer_email)});
            intent.putExtra(Intent.EXTRA_SUBJECT, "All in One Video Downloader");
            startActivity(Intent.createChooser(intent, "Send Email"));
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }
}


