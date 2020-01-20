package com.sinothk.file.selector.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ess.filepicker.FilePicker;
import com.ess.filepicker.model.EssFile;
import com.ess.filepicker.util.Const;
import com.ess.filepicker.util.DialogUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    TextView textView_filename;

    private static final int REQUEST_CODE_CHOOSE = 23;

//    @OnClick(R.id.button_browse)
//    public void onBrowse(View view) {
//
//    }
//
//    @OnClick(R.id.button_scan)
//    public void onScan(View view) {
//
//    }

//    @OnClick(R.id.button_select_pictures)
//    public void onSelectPictures(View view) {
//
//    }

//    @OnClick(R.id.button_fragment)
//    public void onFragment(View view) {
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndPermission
                .with(this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //拒绝权限
                        DialogUtil.showPermissionDialog(MainActivity.this, Permission.transformText(MainActivity.this, permissions).get(0));
                    }
                })
                .start();

        findViewById(R.id.button_browse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePicker.from(MainActivity.this)
                        .chooseForBrowser()
                        .setMaxCount(2)
                        .setFileTypes("png", "doc", "apk", "mp3", "gif", "txt", "mp4", "zip")
                        .requestCode(REQUEST_CODE_CHOOSE)
                        .start();
            }
        });

        findViewById(R.id.button_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePicker
                        .from(MainActivity.this)
                        .chooseForMimeType()
                        .setMaxCount(10)
                        .setFileTypes("png", "doc", "apk", "mp3", "gif", "txt", "mp4", "zip")
                        .requestCode(REQUEST_CODE_CHOOSE)
                        .start();
            }
        });

        findViewById(R.id.button_select_pictures).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePicker
                        .from(MainActivity.this)
                        .chooseMedia()
                        .enabledCapture(true)
                        .setTheme(R.style.FilePicker_Dracula)
                        .requestCode(REQUEST_CODE_CHOOSE)
                        .start();
            }
        });

        findViewById(R.id.button_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FragmentTestActivity.class));
            }
        });


        textView_filename = this.findViewById(R.id.textView_filename);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHOOSE) {
            ArrayList<EssFile> essFileList = data.getParcelableArrayListExtra(Const.EXTRA_RESULT_SELECTION);

            StringBuilder builder = new StringBuilder();

            for (EssFile file : essFileList) {
                builder.append(file.getMimeType()).append(" | ").append(file.getName()).append("\n\n");
            }
            textView_filename.setText(builder.toString());
        }
    }
}
