package xyz.jhnah917.androidproject2019;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ToggleButton;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int btnSelect = 0;
    public static Context ctx;
    public static Boolean power;
    public static String prefix;
    boolean isMenuLoading = false;
    RelativeLayout mainLayout;
    DrawerLayout fullLayout;
    private static final int MESSAGE_PERMISSION_GRANTED = 101;
    private static final int MESSAGE_PERMISSION_DENIED = 102;
    public MainHandler mainHandler = new MainHandler();
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Spinner spin;
    TextView spinnerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this.getApplicationContext();
        showPermissionDialog();
        Util.setGlobalFont(this, getWindow().getDecorView());
        mainLayout = (RelativeLayout) findViewById(R.id.homeLayout);
        fullLayout = (DrawerLayout) findViewById(R.id.fullLayout);
        if(new java.io.File(FileIO.folder).exists() == false)
            new java.io.File(FileIO.folder).mkdirs();
        if(new java.io.File(FileIO.powerPath).exists() == false){
            FileIO.makeFile(FileIO.powerPath);
            FileIO.saveFile(FileIO.powerPath, "true", false);
        }
        if (new java.io.File(FileIO.logPath).exists() == false){
            FileIO.makeFile(FileIO.logPath);
            FileIO.saveFile(FileIO.logPath, "[[로그]]", false);
        }
        if(new java.io.File(FileIO.scriptPath).exists() == false){
            FileIO.makeFile(FileIO.scriptPath);
            String source = "function response(room, msg, sender){\n\t\n}";
            FileIO.saveFile(FileIO.scriptPath, source, false);
        }
        if(new java.io.File(FileIO.prefixPath).exists() == false){
            FileIO.makeFile(FileIO.prefixPath);
            FileIO.saveFile(FileIO.prefixPath, "*", false);
            prefix = "*";
        }
        prefix = FileIO.readFile(FileIO.prefixPath);
        spinnerTxt = findViewById(R.id.spinnerTxt);
        spinnerTxt.setText("명령어 접두어 설정 : " + prefix);
        //home layout
        Button perBtn = (Button) findViewById(R.id.perBtn);
        perBtn.setOnClickListener(getPer);

        ToggleButton botPower = (ToggleButton)findViewById(R.id.botPower);
        if(FileIO.readFile(FileIO.powerPath).equals("true")){
            botPower.setChecked(true); power = true;
        }else{
            botPower.setChecked(false); power = false;
        }
        botPower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    power = true;
                    FileIO.saveFile(FileIO.powerPath, "true", false);
                }else{
                    power = false;
                    FileIO.saveFile(FileIO.powerPath, "false", false);
                }
            }
        });

        Button logBtn = (Button) findViewById(R.id.log);
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.ctx, MainActivity2.class);
                startActivity(intent);
            }
        });

        Button scriptBtn = (Button)findViewById(R.id.script);
        scriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.ctx, Main3Activity.class);
                startActivity(intent);
            }
        });

        list = new ArrayList<>();
        list.add("접두어 설정");
        list.add("*");
        list.add("/");
        list.add("@");
        list.add("!");
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spin = findViewById(R.id.spinner);
        spin.setAdapter(adapter);
        spin.setSelection(0);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0){
                    Toast.makeText(getApplicationContext(),"접두어 : " + list.get(i),
                            Toast.LENGTH_SHORT).show();
                    spin.setSelection(0);
                    FileIO.saveFile(FileIO.prefixPath, list.get(i), false);
                    prefix = FileIO.readFile(FileIO.prefixPath);
                    spinnerTxt.setText("명령어 접두어 설정 : " + prefix);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private Button.OnClickListener getPer = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
    };

    private void showPermissionDialog(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(ctx, "Permission Granted", Toast.LENGTH_SHORT).show();
                mainHandler.sendEmptyMessage(MESSAGE_PERMISSION_GRANTED);
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(ctx, "Permission Denied: " + deniedPermissions.get(0), Toast.LENGTH_SHORT).show();
                mainHandler.sendEmptyMessage(MESSAGE_PERMISSION_DENIED);
            }
        };
        new TedPermission(this).setPermissionListener(permissionListener).setRationaleMessage("파일 접근 권한이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).check();
    }

    private class MainHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case MESSAGE_PERMISSION_DENIED:
                    finish(); break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}