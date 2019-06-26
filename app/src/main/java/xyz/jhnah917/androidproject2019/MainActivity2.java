package xyz.jhnah917.androidproject2019;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;


public class MainActivity2 extends AppCompatActivity {
    public static Context ctx;
    TextView logView;
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ctx = this.getApplicationContext();
        Util.setGlobalFont(this, getWindow().getDecorView());
        logView = findViewById(R.id.logView);
        reset= findViewById(R.id.reset);

        logView.setMovementMethod(new ScrollingMovementMethod());
        logView.setText(FileIO.readFile(FileIO.logPath));

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileIO.saveFile(FileIO.logPath, "[로그]", false);
                logView.setText(FileIO.readFile(FileIO.logPath));
            }
        });
    }
}
