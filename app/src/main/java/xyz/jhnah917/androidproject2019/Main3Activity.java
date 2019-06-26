package xyz.jhnah917.androidproject2019;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xyz.jhnah917.androidproject2019.R;

public class Main3Activity extends AppCompatActivity {
    public static Context ctx;
    EditText scriptView;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ctx = this;
        scriptView = (EditText)findViewById(R.id.scriptView);
        saveBtn = (Button)findViewById(R.id.saveBtn);

        scriptView.setText(FileIO.readFile(FileIO.scriptPath));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileIO.saveFile(FileIO.scriptPath, scriptView.getText().toString(), false);
                xyz.jhnah917.androidproject2019.Listener.initScript();
            }
        });
    }
}
